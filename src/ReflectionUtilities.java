/*
 * Shannon Duvall and <You>
 * This object does basic reflection functions
 */
import java.lang.reflect.*;

public class ReflectionUtilities {
	
	/* Given a class and an object, tell whether or not the object represents 
	 * either an int or an Integer, and the class is also either int or Integer.
	 * This is really yucky, but the reflection things we need like Class.isInstance(arg)
	 * just don't work when the arg is a primitive.  Luckily, we're only worrying with ints.
	 * This method works - don't change it.
	 */
	private static boolean typesMatchInts(Class<?> maybeIntClass, Object maybeIntObj){
		//System.out.println("I'm checking on "+maybeIntObj);
		//System.out.println(maybeIntObj.getClass());
		try{
			return (maybeIntClass == int.class) &&
				(int.class.isAssignableFrom(maybeIntObj.getClass()) || 
						maybeIntObj.getClass()==Class.forName("java.lang.Integer"));
		}
		catch(ClassNotFoundException e){
			return false;
		}
	}
	
	/*
	 * TODO: typesMatch
	 * Takes an array of Classes and an array of Objects and tells whether or not 
	 * the object is an instance of the associated class, and that the two arrays are the
	 * same length.  For objects, the isInstance method makes this easy.  For ints, use the method I 
	 * provided above.  
	 */
	public static boolean typesMatch (Class<?>[] formals, Object[] actuals) {
		if(formals.length != actuals.length)
			return false;

		for(int i = 0; i < formals.length; i++){
			boolean isInstance = formals[i].isInstance(actuals[i]) || typesMatchInts(formals[i], actuals[i]);
			if (!isInstance)
				return false;
		}
		return true;
	}
	
	
	/*
	 * TODO: createInstance
	 * Given String representing fully qualified name of a class and the
	 * actual parameters, returns initialized instance of the corresponding 
	 * class using matching constructor.  
	 * You need to use typeMatch to do this correctly.  Use the class to 
	 * get all the Constructors, then check each one to see if the types of the
	 * constructor parameters match the actual parameters given.
	 */
	public static Object createInstance (String name, Object[] args) {
		try {
			Class c = Class.forName(name);
			Constructor[] constructors = c.getConstructors();
			for (Constructor constructor: constructors){
				if (constructor.getParameterCount() == args.length)
					if(typesMatch(constructor.getParameterTypes(), args))
						return constructor.newInstance(args);
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Class not found when creating instance!");
			e.printStackTrace();

		} catch (InstantiationException e) {
			System.out.println("Problem instantiating!");
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			System.out.println("Illegal Access!");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("InvocationTargetException");
			e.printStackTrace();
		}

		return null;
	}
	
	/*
	 * TODO: callMethod
	 * Given a target object with a method of the given name that takes 
	 * the given actual parameters, call the named method on that object 
	 * and return the result. 
	 * 
	 * If the method's return type is void, null is returned.
	 * 
	 * Again, to do this correctly, you should get a list of the object's 
	 * methods that match the method name you are given.  Then check each one to 
	 * see which has formal parameters to match the actual parameters given.  When
	 * you find one that matches, invoke it.
	 */
	public static Object callMethod (Object target, String name, Object[] args) {
		Class c = target.getClass();
		Method[] methods = c.getDeclaredMethods();
		for (Method method: methods){
			if (method.getName().equals(name) && typesMatch(method.getParameterTypes(), args)){
				try {
					return method.invoke(target, args);

				} catch (IllegalAccessException e) {
					System.out.println("IllegalAccessException");
					e.printStackTrace();

				} catch (InvocationTargetException e) {
					System.out.println("InvocationTargetException");
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
