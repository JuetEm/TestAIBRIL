package testApi;

public class Logger {

	int k;
	public Logger() {
		// TODO Auto-generated constructor stub
		this.k = 0;
	}
	
	public void l(String message) {
		System.out.println(k+". log : "+message);
		k++;
	}

}
