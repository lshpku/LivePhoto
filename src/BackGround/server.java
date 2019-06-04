package BackGround;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import DBInterface.*;


public class server extends Thread {
	public static void main(String[] args) {
		new server().start();
	}

	public void run() {
		System.out.println("server start...");
		ServerSocket s=null;
		try {
			s=new ServerSocket(1999);
			while(true) {
				try {
					Socket s1=s.accept();
					System.out.println("back server receive " + s1.getRemoteSocketAddress());
					InputStream is=s1.getInputStream();
					byte[] Array=new byte[1024];
					String str="";
					int len=0;
					while((len=is.read(Array))!=-1) {
						str+=new String(Array,0,len);
						if(str.indexOf("\n")>0)
							break;
					}
					if(str.length()>0) {
						if(str.charAt(0)=='A') {
							System.out.println("enter A");
							str=str.substring(2);//account=""+" key="+""
							int idx=str.indexOf("key=");
							int idx1=str.indexOf("name=");
							String name=str.substring(idx1+5, idx-1);
							String key=str.substring(idx+4);
							OutputStream os=s1.getOutputStream();
							if(!DBInterface.checkPasswd(name,key)) {
								System.out.println("ACCOUNT NAME " + name);
								System.out.println("account passwd " + key);
								System.out.println("enter false");
								os.write("false".getBytes());
							}else {
								String[] news=DBInterface.getAccountContent(name);
								System.out.println(news.length);
								int arrayLen=0;
								try {
									arrayLen=Integer.valueOf(news[0]);
								}catch(NumberFormatException e) {
									e.printStackTrace();
								}
								int i=0;
								while(i<=arrayLen) {
									try {
										os.write(news[i].getBytes());
									}catch(NullPointerException e) {
										e.printStackTrace();
									}
									i++;
								}
							}
							try {
								os.close();
							}catch(IOException e) {
								e.printStackTrace();
							}
//						r.close();
						}
						else if(str.charAt(0)=='N') {
							int idx=str.indexOf("key=");
							int idx1=str.indexOf("name=");
							String name=str.substring(idx1+5, idx-1);
							String key=str.substring(idx+4);
							DBInterface.createAccount(name, key);
						}
						else if(str.charAt(0)=='D') {
							String num=str.substring(2);
							int n=Integer.parseInt(num);
							DBInterface.deleteTheme(n);
						}
						else if(str.charAt(0)=='E') {
							OutputStream output=s1.getOutputStream();
							int num=DBInterface.establishTheme(str.substring(2));
							byte[] tmpArray=String.valueOf(num).getBytes();
							output.write(tmpArray);
							if(output!=null) {
								try {
									output.close();
								}catch(IOException e) {
									e.printStackTrace();
								}
							}
						}
						else {//�ϴ�ͼƬ���֣������ݷ��͸����ݿ�
							int idx1=str.indexOf("photo=");
							FileOutputStream fout1=new FileOutputStream("~/root/LivePhoto/src/BackGround/test.jpg");
							while((len=is.read(Array))!=-1) {
								fout1.write(Array);
							}
							fout1.close();
							FileInputStream fin=new FileInputStream(new File("~/root/LivePhoto/src/BackGround/test.jpg"));
							byte[] bytes=new byte[fin.available()];
							fin.read(bytes);
							DBInterface.sendInfo(str.substring(2),bytes);
						}
					}
					is.close();
					s1.close();
				}catch(IOException e) {}
			}
		}catch(IOException e) {
			System.out.println("server error...");
		}
	}
}
