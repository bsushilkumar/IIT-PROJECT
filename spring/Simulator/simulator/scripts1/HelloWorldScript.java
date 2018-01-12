package simulator.scripts1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloWorldScript {

	public static void main(String[] args) {
		String cmd = "ls src/util";
		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr
					.getInputStream()));
			String line = "";
			while ((line = buf.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
