// `^`^`^`
// ```java
// /**
//  * This class provides a utility method to suppress warnings in IntelliJ IDEA when initializing static final fields with null values.
//  *
//  * Class: InjectionUtil
//  * Package: invmod.util
//  *
//  * The InjectionUtil class contains a single method:
//  *
//  * - Null(): This generic method serves as a workaround for suppressing the "Argument might be null" warnings in IntelliJ IDEA. It is intended to be used in place of the literal null when initializing static final fields. The method returns null, but by using this method, developers can avoid the warnings that would typically be triggered by directly assigning null to a field. This approach is based on a solution proposed by diesieben07 in the Minecraft Forge forums. The method is annotated with @SuppressWarnings to prevent the "ConstantConditions" and "SameReturnValue" warnings from being raised by the IDE's static code analysis.
//  *
//  * Note: This utility is specifically tailored for IntelliJ IDEA users. Eclipse users have alternative means to suppress such warnings using @SuppressWarnings annotation.
//  */
// package invmod.util;
// 
// public class InjectionUtil {
//     // Method implementations...
// }
// ```
// `^`^`^`

package invmod.util;

public class InjectionUtil {
	/**
	 * Returns <code>null</code>.
	 * <p>
	 * This is used in the initialisers of <code>static final</code> fields instead
	 * of using <code>null</code> directly to suppress the "Argument might be null"
	 * warnings from IntelliJ IDEA's "Constant conditions &amp; exceptions"
	 * inspection.
	 * 
	 * Note by Toaster: That seems to be an intelliJ only problem, for eclipse:
	 * There is @SuppressWarnings for that stuff ;)
	 * 
	 * <p>
	 * Based on diesieben07's solution <a href=
	 * "http://www.minecraftforge.net/forum/topic/60980-solved-disable-%E2%80%9Cconstant-conditions-exceptions%E2%80%9D-inspection-for-field-in-intellij-idea/?do=findCommentcomment=285024">here</a>.
	 *
	 * @param <T> The field's type.
	 * @return null
	 */
	@SuppressWarnings({ "ConstantConditions", "SameReturnValue" })
	public static <T> T Null() {
		return null;
	}
}
