// `^`^`^`
// ```java
// /**
//  * This code establishes a packet handling system for a Minecraft mod, allowing for the structured and efficient
//  * communication between the client and server. It uses the Netty networking library and Forge Mod Loader (FML)
//  * for handling custom packet data within the Minecraft modding context.
//  *
//  * PacketPipeline Class:
//  * - Extends MessageToMessageCodec to encode and decode packet data.
//  * - Manages an EnumMap of channels for different network sides (CLIENT/SERVER).
//  * - Maintains a list of registered packet classes and ensures a limit of 256 packets.
//  * - Provides methods to initialize and post-initialize the packet pipeline, sorting packets for consistency.
//  * - Contains methods to send packets to various targets: all players, a specific player, around a point, within a dimension, and to the server.
//  *
//  * Methods:
//  * - registerPacket(Class<? extends AbstractPacket> clazz): Registers packet classes with automatic discriminators.
//  * - encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out): Encodes packets, setting discriminators and converting to FMLProxyPacket.
//  * - decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out): Decodes FMLProxyPacket into AbstractPacket and handles it based on the network side.
//  * - initialise(): Initializes channels for network communication.
//  * - postInitialise(): Sorts registered packets and finalizes initialization.
//  * - getClientPlayer(): [Client-side] Retrieves the current player on the client side.
//  * - sendToAll(AbstractPacket message): Sends a packet to all players on the server.
//  * - sendTo(AbstractPacket message, EntityPlayerMP player): Sends a packet to a specific player.
//  * - sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point): Sends a packet to all players around a specific point.
//  * - sendToDimension(AbstractPacket message, int dimensionId): Sends a packet to all players within a specific dimension.
//  * - sendToServer(AbstractPacket message): Sends a packet from the client to the server.
//  *
//  * Usage:
//  * - Call initialise() during FMLInitializationEvent.
//  * - Call postInitialise() after all packets have been registered, during FMLPostInitializationEvent.
//  * - Use the sendTo* methods to dispatch packets to players or the server as needed.
//  *
//  * Note:
//  * - This code is designed to work with Minecraft Forge for Minecraft version 1.7.10.
//  * - The AbstractPacket class is assumed to be a part of the mod's packet handling system and must be implemented elsewhere.
//  */
// ```
// `^`^`^`

//package invmod.packethandling;
//
//import java.util.*;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.MessageToMessageCodec;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.network.INetHandler;
//import net.minecraft.network.NetHandlerPlayServer;
//
//import cpw.mods.fml.common.FMLCommonHandler;
//import cpw.mods.fml.common.network.FMLEmbeddedChannel;
//import cpw.mods.fml.common.network.FMLOutboundHandler;
//import cpw.mods.fml.common.network.NetworkRegistry;
//import cpw.mods.fml.common.network.internal.FMLProxyPacket;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
///**
// * Packet pipeline class. Directs all registered packet data to be handled by the packets themselves.
// * @author sirgingalot
// * some code from: cpw
// */
//@ChannelHandler.Sharable
//public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket> {
//
//    private EnumMap<Side, FMLEmbeddedChannel>           channels;
//    private LinkedList<Class<? extends AbstractPacket>> packets           = new LinkedList<Class<? extends AbstractPacket>>();
//    private boolean                                     isPostInitialised = false;
//
//    /**
//     * Register your packet with the pipeline. Discriminators are automatically set.
//     *
//     * @param clazz the class to register
//     *
//     * @return whether registration was successful. Failure may occur if 256 packets have been registered or if the registry already contains this packet
//     */
//    public boolean registerPacket(Class<? extends AbstractPacket> clazz) {
//        if (this.packets.size() > 256) {
//            // You should log here!!
//            return false;
//        }
//
//        if (this.packets.contains(clazz)) {
//            // You should log here!!
//            return false;
//        }
//
//        if (this.isPostInitialised) {
//            // You should log here!!
//            return false;
//        }
//
//        this.packets.add(clazz);
//        return true;
//    }
//
//    // In line encoding of the packet, including discriminator setting
//    @Override
//    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception {
//        ByteBuf buffer = Unpooled.buffer();
//        Class<? extends AbstractPacket> clazz = msg.getClass();
//        if (!this.packets.contains(msg.getClass())) {
//            throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
//        }
//
//        byte discriminator = (byte) this.packets.indexOf(clazz);
//        buffer.writeByte(discriminator);
//        msg.encodeInto(ctx, buffer);
//        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
//        out.add(proxyPacket);
//    }
//
//    // In line decoding and handling of the packet
//    @Override
//    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
//        ByteBuf payload = msg.payload();
//        byte discriminator = payload.readByte();
//        Class<? extends AbstractPacket> clazz = this.packets.get(discriminator);
//        if (clazz == null) {
//            throw new NullPointerException("No packet registered for discriminator: " + discriminator);
//        }
//
//        AbstractPacket pkt = clazz.newInstance();
//        pkt.decodeInto(ctx, payload.slice());
//
//        EntityPlayer player;
//        switch (FMLCommonHandler.instance().getEffectiveSide()) {
//            case CLIENT:
//                player = this.getClientPlayer();
//                pkt.handleClientSide(player);
//                break;
//
//            case SERVER:
//                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
//                player = ((NetHandlerPlayServer) netHandler).playerEntity;
//                pkt.handleServerSide(player);
//                break;
//
//            default:
//        }
//
//        out.add(pkt);
//    }
//
//    // Method to call from FMLInitializationEvent
//    public void initialise() {
//        this.channels = NetworkRegistry.INSTANCE.newChannel("TUT", this);
//    }
//
//    // Method to call from FMLPostInitializationEvent
//    // Ensures that packet discriminators are common between server and client by using logical sorting
//    public void postInitialise() {
//        if (this.isPostInitialised) {
//            return;
//        }
//
//        this.isPostInitialised = true;
//        Collections.sort(this.packets, new Comparator<Class<? extends AbstractPacket>>() {
//
//            @Override
//            public int compare(Class<? extends AbstractPacket> clazz1, Class<? extends AbstractPacket> clazz2) {
//                int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
//                if (com == 0) {
//                    com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
//                }
//
//                return com;
//            }
//        });
//    }
//
//    @SideOnly(Side.CLIENT)
//    private EntityPlayer getClientPlayer() {
//        return Minecraft.getMinecraft().thePlayer;
//    }
//
//    /**
//     * Send this message to everyone.
//     * <p/>
//     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
//     *
//     * @param message The message to send
//     */
//    public void sendToAll(AbstractPacket message) {
//        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
//        this.channels.get(Side.SERVER).writeAndFlush(message);
//    }
//
//    /**
//     * Send this message to the specified player.
//     * <p/>
//     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
//     *
//     * @param message The message to send
//     * @param player  The player to send it to
//     */
//    public void sendTo(AbstractPacket message, EntityPlayerMP player) {
//        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
//        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
//        this.channels.get(Side.SERVER).writeAndFlush(message);
//    }
//
//    /**
//     * Send this message to everyone within a certain range of a point.
//     * <p/>
//     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
//     *
//     * @param message The message to send
//     * @param point   The {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint} around which to send
//     */
//    public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point) {
//        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
//        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
//        this.channels.get(Side.SERVER).writeAndFlush(message);
//    }
//
//    /**
//     * Send this message to everyone within the supplied dimension.
//     * <p/>
//     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
//     *
//     * @param message     The message to send
//     * @param dimensionId The dimension id to target
//     */
//    public void sendToDimension(AbstractPacket message, int dimensionId) {
//        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
//        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
//        this.channels.get(Side.SERVER).writeAndFlush(message);
//    }
//
//    /**
//     * Send this message to the server.
//     * <p/>
//     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
//     *
//     * @param message The message to send
//     */
//    public void sendToServer(AbstractPacket message) {
//        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
//        this.channels.get(Side.CLIENT).writeAndFlush(message);
//    }
//}
