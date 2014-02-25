package javaSockets;

public class blablba {
	public static void main(String argv[]) throws Exception {
		String a = "GET   Hello";
		String b = "GET Hello";
		
		String[] pi1 = a.split(" ");
		String[] pi2 = b.split(" ");
		System.out.println("Dit is de eerste:" + " "+pi2.length + "-"+pi1.length);
		for(int i = 0; i< pi1.length; i++ ){
			System.out.println(pi1[i]);
		}
		System.out.println("De tweede");
		for(int i = 0; i < pi2.length; i++ ){
			System.out.println(pi2[i]);
		}
	}
}
