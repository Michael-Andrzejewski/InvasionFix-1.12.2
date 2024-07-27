// `^`^`^`
// ```java
// /**
//  * Executive Summary:
//  * This code defines an abstract class named AbstractPacket that serves as a template for network packets in a Minecraft mod. The class is designed to be extended by other packet classes that are intended to be used with a PacketPipeline system for handling network communication. The class contains four abstract methods that must be implemented by subclasses to handle the encoding, decoding, and processing of packet data on both the client and server sides.
//  *
//  * Methods:
//  * - encodeInto(ChannelHandlerContext ctx, ByteBuf buffer): This method is responsible for encoding the packet data into a ByteBuf stream for transmission. Implementations should handle the conversion of complex data types using appropriate data handlers.
//  *
//  * - decodeInto(ChannelHandlerContext ctx, ByteBuf buffer): This method is responsible for decoding packet data from a ByteBuf stream upon receipt. Implementations should handle the conversion of complex data types using appropriate data handlers.
//  *
//  * - handleClientSide(EntityPlayer player): This method is called to handle the packet on the client side after the packet has been decoded. Implementations should define how the packet data affects the client, typically involving the player who received the packet.
//  *
//  * - handleServerSide(EntityPlayer player): This method is called to handle the packet on the server side after the packet has been decoded. Implementations should define how the packet data affects the server, typically involving the player who sent the packet.
//  *
//  * Note: This class is abstract and does not provide concrete implementations of its methods. Subclasses must provide these implementations for the packet to be functional within the game's network system.
//  */
// ```
// `^`^`^`

//package invmod.packethandling;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//
//import net.minecraft.entity.player.EntityPlayer;
//
//
///**
// * AbstractPacket class. Should be the parent of all packets wishing to use the PacketPipeline.
// * @author sirgingalot
// */
//public abstract class AbstractPacket {
//
//    /**
//     * Encode the packet data into the ByteBuf stream. Complex data sets may need specific data handlers (See @link{cpw.mods.fml.common.network.ByteBuffUtils})
//     *
//     * @param ctx    channel context
//     * @param buffer the buffer to encode into
//     */
//    public abstract void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer);
//
//    /**
//     * Decode the packet data from the ByteBuf stream. Complex data sets may need specific data handlers (See @link{cpw.mods.fml.common.network.ByteBuffUtils})
//     *
//     * @param ctx    channel context
//     * @param buffer the buffer to decode from
//     */
//    public abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer);
//
//    /**
//     * Handle a packet on the client side. Note this occurs after decoding has completed.
//     *
//     * @param player the player reference
//     */
//    public abstract void handleClientSide(EntityPlayer player);
//
//    /**
//     * Handle a packet on the server side. Note this occurs after decoding has completed.
//     *
//     * @param player the player reference
//     */
//    public abstract void handleServerSide(EntityPlayer player);
//}