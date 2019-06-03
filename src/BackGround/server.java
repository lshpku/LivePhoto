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
		ServerSocket s=null;
		try {
			s=new ServerSocket(1999);
		}catch(IOException e) {}
		while(true) {
			try {
				Socket s1=s.accept();
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
					if(str.charAt(0)=='A') {//��¼�˻���������Ӧ����������&���
						str=str.substring(2);//account=""+" key="+""
						int idx=str.indexOf("key=");
						int idx1=str.indexOf("name=");
						String name=str.substring(idx1+5, idx-1);
						String key=str.substring(idx+4);
						OutputStream os=s1.getOutputStream();
						if (!DBInterface.checkPasswd(name, key)){
							os.write("false".getBytes());
						}
						else{
							ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
							HashMap<String, ArrayList<Object>> news=DBInterface.getAccountContent(name);
							try {
								out.writeObject(news);
								out.close();
							}catch(NullPointerException e) {
								e.printStackTrace();
							}
						}
//						String f="~/root/LivePhoto/src/BackGround/account.txt";
//						BufferedReader r=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
//						String line="";
//						boolean accountExist=false;
//						OutputStream os=s1.getOutputStream();
//						while((line=r.readLine())!=null&&line.length() > 0) {
//							r.readLine();
//							int lineidx=line.indexOf("key=");
//							int nameLen=name.length();int keyLen=key.length()-1;int len1=line.length();
//							int i=0;
//							for(i=0;i<nameLen&&i<=lineidx-1;++i) {
//								if(name.charAt(i)!=line.charAt(i))
//									break;
//							}
//							if(i==nameLen) {
//								for(i=0;i<keyLen&&i<len1;++i) {
//									if(key.charAt(i)!=line.charAt(lineidx+4+i))
//										break;
//								}
//							}
//							if(i==keyLen) {
//								accountExist=true;
//								ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
//								HashMap<String, ArrayList<Object>> news=DBInterface.getAccountContent(name);
//								try {
//									out.writeObject(news);
//									out.close();
//								}catch(NullPointerException e) {
//									e.printStackTrace();
//								}
//								break;
//							}
//						}
//						if(!accountExist) {
//							os.write("false".getBytes());
//						}
						if(os!=null) {
							try {
								os.close();
							}catch(IOException e) {
								e.printStackTrace();
							}
						}
//						r.close();
					}
					else if(str.charAt(0)=='N') {//�½��˻��������ݴ洢����������ȥ
						int idx=str.indexOf("key=");
						int idx1=str.indexOf("name=");
						String name=str.substring(idx1+5, idx-1);
						String key=str.substring(idx+4);
						DBInterface.createAccount(name, key);
//						String f="~/root/LivePhoto/src/BackGround/account.txt";
//						FileOutputStream fo=new FileOutputStream(new File(f),true);
//						int idx=str.indexOf("name=");
//						str+=System.getProperty("line.separator");
//						fo.write(str.substring(idx+5).getBytes());
//						fo.close();
					}
					else if(str.charAt(0)=='D') {//ɾ���������⣬����Ϣ���͸����ݿ�
						String num=str.substring(2);
						int n=Integer.parseInt(num);
						DBInterface.deleteTheme(n);
					}
					else if(str.charAt(0)=='E') {//�½��������⣬�����ݷ��͸����ݿ�
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
	}
}
