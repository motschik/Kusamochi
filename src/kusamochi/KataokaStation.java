package kusamochi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import twitmochi.TweetTest;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class KataokaStation extends JFrame{

	/**
	 *
	 */
	private static final long serialVersionUID = 2924798940571767292L;
	public static String conkey;
	public static String consec;

	static JTextArea Tf = new JTextArea();
	static JScrollPane scr = new JScrollPane(Tf,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	static JPanel pnl = new JPanel();
	static KataokaStation frm = new KataokaStation();

	static int acountSel;
	static Acount[] acount;
	static int acosu;
	static String list;
	String tweet;

	static ImageIcon icos;
	static String version = "1.0";

	public static void main(String[] args){
		Properties ppt = new Properties();
		FileInputStream in = null;
	    try {
	      in = new FileInputStream("property.properties");
	      ppt.load(in);
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	      if (in != null) {
	        try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	      }
	    }
	    conkey = ppt.getProperty("conkey");
	    consec = ppt.getProperty("consec");

		File ver = new File(".\\ver");

    	if(ver.exists() == false){
    		ver.mkdir();
    	}
		try{
			FileOutputStream fos = null;
        	fos = new FileOutputStream(".\\ver\\KMVN.dat");
        	DataOutputStream dos = new DataOutputStream(fos);
        	dos.writeUTF(version);
        	fos.close();
        	dos.close();
		}catch(Exception ee){
			System.out.println(ee + "です");
		}


	/**/FileInputStream fis = null;
		DataInputStream dis = null;

		/*list.dat�ǂݍ���*/
		//String list;
		String listary[] = new String[0];
		try{
			fis = new FileInputStream(".\\saves\\acount\\list.dat");
			dis = new DataInputStream(fis);
			list = dis.readUTF();
			fis.close();
			dis.close();
			System.out.println(list);
			if(list.length() != 0){
				listary = list.split("\n");
			}
		}catch(Exception e){
			list = "";
			listary = new String[0];
			System.out.println(e);
		}

		System.out.println(listary.length);
		acosu = listary.length;
		acount = new Acount[acosu];
		for(int i = 0;i < acosu;i = i + 1){
			acount[i] = new Acount();
		}


		/*last.dat�ǂݍ���*/
		try{
			fis = new FileInputStream(".\\saves\\acount\\last.dat");
			dis = new DataInputStream(fis);
			acountSel = dis.readInt();
			fis.close();
			dis.close();
		}catch(Exception e){
			acountSel = 0;
			try{
				FileOutputStream fos = null;
	        	fos = new FileOutputStream(".\\saves\\acount\\last.dat");
	        	DataOutputStream dos = new DataOutputStream(fos);
	        	dos.writeInt(acountSel);
	        	fos.close();
	        	dos.close();
			}catch(Exception ee){

			}
			System.out.println(e);
		}



		for(int i = 0;i < acosu;i = i + 1){
			System.out.println("hajimefor");
			int acoorder;
			acoorder = Acount.inputOrder(listary[i]);
			System.out.println("listary = " + listary[i]);
			acount[acoorder].inputDat(listary[i]);

			Twitter twitter = new TwitterFactory().getInstance();
			AccessToken accesstoken = new AccessToken(acount[acoorder].token ,acount[acoorder].secret);
			twitter.setOAuthConsumer(conkey,consec);
			twitter.setOAuthAccessToken(accesstoken);
			String twiName;
			try{
				twiName = twitter.getScreenName();
			}catch(Exception e){
				twiName = acount[acoorder].name;
				System.out.println(e);
			}
			if(twiName != acount[acoorder].name){
				acount[acoorder].name = twiName;
				listary[i] = twiName;
			}
		}

		list = "";
	//	if(acosu == 0)list = ".";
		for(int i = 0;i < acosu;i = i + 1){
			if(i == 0){
				list = (list + listary[i]);
			}else{
				list = (list + "\n" + listary[i]);
			}
		}
		try{
        	FileOutputStream fos = null;
        	fos = new FileOutputStream(".\\saves\\acount\\list.dat");
        	DataOutputStream dos = new DataOutputStream(fos);
        	dos.writeUTF(list);
        	fos.close();
        	dos.close();
		}catch(Exception e){

		}


		try{
		fis = new FileInputStream(".\\saves\\tag.dat");
		dis = new DataInputStream(fis);
		Acount.hashu = dis.readUTF();
		fis.close();
		dis.close();
		}catch(Exception e){
			System.out.println(e);
		}

		try{
			frm.setTitle("@"+acount[acountSel].name);
		}catch(Exception e){
			frm.setTitle("KUSAMOCHi -N- Ver." + version);
		}
		frm.setIconImage(icos.getImage());
		frm.setResizable(true);
		frm.setAlwaysOnTop(true);

		Dimension dms = Toolkit.getDefaultToolkit().getScreenSize();
		frm.setBounds((int)dms.getWidth()-310,(int)dms.getHeight()-130,300,87);

		scr.setSize(frm.getWidth()-15,frm.getHeight()-37);
		Tf.setSize(frm.getWidth()-15,frm.getHeight()-37);

		frm.setVisible(true);
		frm.setDefaultCloseOperation(EXIT_ON_CLOSE);

		/*
		TLThread thr = new TLThread();
		thr.start();
		*/

	}

	static DataInputStream Read(String str){
		DataInputStream dis = null;
		try{
		dis = new DataInputStream(new FileInputStream(str));
		}catch(Exception e){
			System.out.println(e);
		}

		return(dis);
	}

	public class ResizeListener implements ComponentListener{
		public void componentResized(ComponentEvent e){
			scr.setSize(frm.getWidth()-15,frm.getHeight()-37);
			Tf.setSize(frm.getWidth()-15,frm.getHeight()-37);
		}

		public void componentHidden(ComponentEvent e){

		}

		public void componentMoved(ComponentEvent e){

		}
		public void componentShown(ComponentEvent e){

		}
	}

	public KataokaStation(){
		//System.setProperty("twitter4j.http.useSSL", "true");
	//	JPanel pnl = new JPanel();。
		ClassLoader cl = this.getClass().getClassLoader();
		icos = new ImageIcon(cl.getResource("aikos.png"));
		pnl.setLayout(null);
		addComponentListener(new ResizeListener());

		Tf.addKeyListener(new EnterListener());
		Tf.addMouseListener(new RightButton());
		Tf.addMouseWheelListener(new AcountListener());
		Tf.setLineWrap(true);

		pnl.add(scr);
		//pnl.add(Tf);

		add(pnl);

	}

	public class EnterListener implements KeyListener{

		boolean key = false;
		boolean shift = false;
		boolean next = false;
		boolean back = false;
		boolean right = false;
		boolean left = false;
		public void keyTyped(KeyEvent e){

		}

		public void keyPressed(KeyEvent e){

			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if(shift){
					Tf.insert("\n", Tf.getCaretPosition());
				}else if(!key){
					if(acosu != 0){
						tweet = Tf.getText();
						SwingWorker<Object,Object> worker = new Tweets(tweet);
						worker.execute();


						key = true;
					}else{
						Tf.setText("アカウントを認証してください。(左クリック→設定)");
						Tf.setCaretPosition(0);
						Tf.setSelectionEnd(Tf.getText().length());
					}
				}


			}else if(e.getKeyCode() == KeyEvent.VK_SHIFT){
				shift = true;
			}else if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) && shift){
				KataokaStation.acountSel = KataokaStation.acountSel + 1;
				if(KataokaStation.acountSel > KataokaStation.acosu-1){
					KataokaStation.acountSel = 0;
				}
				KataokaStation.frm.setTitle("@" + KataokaStation.acount[KataokaStation.acountSel].name);
			}else if((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) && shift){
				KataokaStation.acountSel = KataokaStation.acountSel - 1;
				if(KataokaStation.acountSel < 0){
					KataokaStation.acountSel = KataokaStation.acosu-1;
				}
				KataokaStation.frm.setTitle("@" + KataokaStation.acount[KataokaStation.acountSel].name);
			}

		}

		public void keyReleased(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ENTER && key){
				key = false;

					Tf.setText(null);
					Tf.setCaretPosition(0);

			}
			if(e.getKeyCode() == KeyEvent.VK_SHIFT){
				shift = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				//key = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_UP||e.getKeyCode() == KeyEvent.VK_LEFT){
				back = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN||e.getKeyCode() == KeyEvent.VK_RIGHT){
				next = false;
			}
		}
	}

	public class RightButton implements MouseListener{
		public RightButton(){
		}

		public void mouseClicked(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			mousePopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			mousePopup(e);
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		private void mousePopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				//ポップアップメニューを表示
				JComponent c = (JComponent)e.getSource();
				showPopup(c, e.getX(), e.getY());
				e.consume();
			}
		}

		private void showPopup(JComponent c, int x, int y) {
			JPopupMenu pmenu = new JPopupMenu("ポップアップメニュー"); //���ɂ���Ă̓��j���[�ɕ\�������炵��

			JMenu acountmenu = new JMenu("アカウント切替");
			JMenuItem menuAco[] = new JMenuItem[acosu];
			for(int i = 0;i < acosu;i = i + 1){
				menuAco[i] = new JMenuItem(acount[i].name);
				menuAco[i].addActionListener(new acountSelect(i));
				acountmenu.add(menuAco[i]);
			}

			JMenuItem option = new JMenuItem("設定");
			option.setAction(new optionAction());
			JMenuItem kusaru = new JMenuItem("終了");
			kusaru.setAction(new kusaruAction());

			pmenu.add(acountmenu);
			pmenu.add(option);
			pmenu.addSeparator();
			pmenu.add(kusaru);

			pmenu.show(c, x, y);	//ポップアップメニューの表示
		}

		public class optionAction extends AbstractAction{
			private static final long serialVersionUID = 1L;
			public optionAction() {
				super("設定");		//メニューに表示される名前
				putValue(ACCELERATOR_KEY,		//アクセラレーターショートカットキー
					KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK)
				);
			}

			public void actionPerformed(ActionEvent e){
				new Option();
			}
		}

		public class acountSelect extends AbstractAction{
			/**
			 *
			 */
			private static final long serialVersionUID = 2080959939123450078L;
			public acountSelect(int i){
			//	super(acount[i].name);		//メニューに表示される名前
			}
			public void actionPerformed(ActionEvent e){
				System.out.println(e.getActionCommand());
				int i3 = -1;
				do{
					i3 = i3 + 1;
				}while(e.getActionCommand() != acount[i3].name);
				acountSel = i3;
				try{
					FileOutputStream fos = null;
		        	fos = new FileOutputStream(".\\saves\\acount\\last.dat");
		        	DataOutputStream dos = new DataOutputStream(fos);
		        	dos.writeInt(acountSel);
		        	fos.close();
		        	dos.close();
				}catch(Exception ee){
					System.out.println(e);
				}
				frm.setTitle("@" +acount[i3].name);
			}
		}
		public class kusaruAction extends AbstractAction{
			private static final long serialVersionUID = 1L;
			public kusaruAction() {
				super("終了");		//���j���[�ɕ\������閼�O
				putValue(ACCELERATOR_KEY,		//�A�N�Z�����[�^�[(�V���[�g�J�b�g)�L�[
					KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK)
				);
			}
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		}

	}

	public class AcountListener implements MouseWheelListener{
		public void mouseWheelMoved(MouseWheelEvent mwe){
			if(mwe.getUnitsToScroll() > 0){
				KataokaStation.acountSel = KataokaStation.acountSel + 1;
				if(KataokaStation.acountSel > KataokaStation.acosu-1){
					KataokaStation.acountSel = 0;
				}
				KataokaStation.frm.setTitle("@" + KataokaStation.acount[KataokaStation.acountSel].name);
			}else{
				KataokaStation.acountSel = KataokaStation.acountSel - 1;
				if(KataokaStation.acountSel < 0){
					KataokaStation.acountSel = KataokaStation.acosu-1;
				}
				KataokaStation.frm.setTitle("@" + KataokaStation.acount[KataokaStation.acountSel].name);
			}
			//System.out.println(mwe.getUnitsToScroll() + "__"+ mwe.getScrollAmount());
		}
	}

	public class Tweets extends SwingWorker<Object, Object>{
		String des;


		public Tweets(String twet){
			super();
			des = twet;
		}

		@Override
	    protected Object doInBackground() throws Exception {

		//���Ԃ̂����鏈�����̓��\�b�h//
			boolean bln = true;
			int akakae = 0;
			if(des != ""){
				if(Acount.hashu != ""){
					des = des + " " + Acount.hashu;
				}
				//for(int i = 0;;i = i + 1){
					try{
						bln = false;
						TweetTest.main(des,acount[acountSel].token,acount[acountSel].secret);

					}catch(TwitterException e){
						String code = e.getErrorMessage();
						System.out.println("���[�ǂ�"+code);
						if(code.equals("Status is a duplicate.")){
							System.out.println("uoo");
							bln = true;
							des = des + "�@";
						}
						if(code.equals("User is over daily status update limit.")){
							System.out.println("����Ȱ");

							Twitter twitter = new TwitterFactory().getInstance();
							twitter.setOAuthConsumer(conkey,consec);
							twitter.setOAuthAccessToken(new AccessToken(KataokaStation.acount[KataokaStation.acountSel].token,KataokaStation.acount[KataokaStation.acountSel].secret));

							Paging pgg = new Paging();
							pgg.setCount(1);
							pgg.setPage(126);

							ResponseList<Status> twitterStatuses = null;

							try{
								twitterStatuses = twitter.getUserTimeline(pgg);
							}catch(Exception ee){}
							Status stt = null;
							for(Status sts:twitterStatuses){
								stt = sts;
							}
							SimpleDateFormat sdf = new SimpleDateFormat("HH'��'mm'��'ss");
							Date date = stt.getCreatedAt();
							date.setTime(date.getTime() + 10800000);
							String kisekaijo = "@" + KataokaStation.acount[KataokaStation.acountSel].name + "Post規制"+sdf.format(date)+"���炢�Ǝv���܂��B";
							System.out.println(kisekaijo);

							//KataokaStation.Tf.setText(".@" + KataokaStation.acount[KataokaStation.acountSel].name + " ���K�����ꂽ�I�I");
							KataokaStation.acountSel = KataokaStation.acountSel + 1;
							if(KataokaStation.acountSel > KataokaStation.acosu-1){
								KataokaStation.acountSel = 0;
							}
							KataokaStation.frm.setTitle("@" + KataokaStation.acount[KataokaStation.acountSel].name);
							bln = true;

							SwingWorker<Object, Object> workers = new Tweets(kisekaijo);
							workers.execute();

							akakae = akakae + 1;
							if(akakae > KataokaStation.acosu){
								bln = false;
								System.out.println("�S�K���ȁ[������");
							}
						}
					}
				//}
			}
		return null;
		}


	}
}

class Option extends JDialog{
	/**
	 *
	 */
	private static final long serialVersionUID = 6140572337415765867L;
	static Twitter twitter;
	static JTextField nom = new JTextField();
	static RequestToken requestToken = null;
	static AccessToken accessToken = null;
	static JTextField hashuT = null;
	static JLabel acountlist[];
	static JPanel acountpnl;

	{
		nom.setText("");
	}
	public Option(){
		setLayout(null);
		Dimension dms = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((int)dms.getWidth()/2-200,(int)dms.getHeight()/2-200,400,250);
		setResizable(false);
		setTitle("設定");
		setModal(true);

		JButton acountadd = new JButton("アカウント追加");
		acountadd.setBounds(10,10,200,20);
		acountadd.addActionListener(new AcountBtn());
		add(acountadd);


		nom.setBounds(60,35,85,20);
		add(nom);

		JLabel pinsetsu = new JLabel("PIN入力");
		//JLabel pinsetsu2 = new JLabel("↓に入力してください");
		pinsetsu.setBounds(10,35,160,20);
		//pinsetsu2.setBounds(40,85,160,20);
		add(pinsetsu);
		//add(pinsetsu2);

		JButton nisho = new JButton("認証");
		nisho.setBounds(150,35,60,20);
		nisho.addActionListener(new NishoBtn());
		add(nisho);

		acountpnl = new JPanel();
		acountpnl.setLayout(null);
		acountpnl.setSize(160,180);

		acountlist = new JLabel[KataokaStation.acosu];
		for(int i = 0;i < KataokaStation.acosu;i = i + 1){

			acountlist[i] = new JLabel(KataokaStation.acount[i].name);
			acountlist[i].setBounds(0,i*20,200,20);
			acountlist[i].setOpaque(true);
			acountlist[i].setBackground(Color.WHITE);
			acountlist[i].addMouseListener(new RightBtn(i));
			acountpnl.add(acountlist[i]);
		}

		JScrollPane acountscr = new JScrollPane(acountpnl);
		acountscr.setBounds(220,10,160,180);
		add(acountscr);



		JLabel hashu = new JLabel();
		hashu.setText("ハッシュタグ");
		hashu.setBounds(10,145,100,20);
		add(hashu);
		hashuT = new JTextField(Acount.hashu);
		hashuT.setBounds(10,170,200,20);
		add(hashuT);

		JButton fin = new JButton("完了");
		fin.setBounds(170,195,60,20);
		fin.addActionListener(new finBtn());
		add(fin);

		setVisible(true);
	}

	public class AcountBtn implements ActionListener{
		public void actionPerformed(ActionEvent e){
			nom.setText(null);
			twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(KataokaStation.conkey,KataokaStation.consec);


            try{
            requestToken = twitter.getOAuthRequestToken();
            }catch(Exception ee){
            	System.out.println("�Ȃɂ�ye����������");
            }
            String url = "";
            try{
            	url = requestToken.getAuthorizationURL();
            }catch(Exception ee){
            	System.out.println("�Ȃɂ�fa������������");
            }
			try{
				new DesktopSample1(url);
			}catch(Exception ee){}
		}
	}
	public class NishoBtn implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String pin = nom.getText();


			try{
                if(pin.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                   /* String twitoken,twisecret;
                    */
                    String Opname;
                    Opname = twitter.getScreenName();
                    boolean sude = true;
                    for(int i = 0;i < KataokaStation.acosu;i = i + 1){
                    	if(KataokaStation.acount[i].name.equals(Opname)){
                    		nom.setText("すでに認証されてます。");
                    		sude = false;
                    		break;
                    	}
                    }
                    if(sude){
                    	KataokaStation.frm.setTitle("@"+Opname);

                    	Acount ac[] = new Acount[KataokaStation.acount.length + 1];
                    	System.arraycopy(KataokaStation.acount, 0, ac, 0, KataokaStation.acosu);
                    	KataokaStation.acount = ac;
                    	KataokaStation.acosu = KataokaStation.acosu + 1;
                    	KataokaStation.acount[KataokaStation.acosu - 1] = new Acount();

                    	//System.out.println(KataokaStation.acount[0].name);


                    	int kal= KataokaStation.acosu - 1;
                  //  	System.out.println(kal);
                    	KataokaStation.acount[kal].token = accessToken.getToken();
                        KataokaStation.acount[kal].secret = accessToken.getTokenSecret();
                        KataokaStation.acount[kal].name = Opname;

                        KataokaStation.acountSel = kal;

                        System.out.println(KataokaStation.acount[0].name);
                        try{
                        System.out.println(KataokaStation.acount[1].name);
                        }catch(Exception ee){}
                        if(KataokaStation.list == ""){
                        	KataokaStation.list = KataokaStation.list + Opname;
                        }else{
                        	KataokaStation.list = KataokaStation.list +"\n" + Opname;
                        }
                    	File saves = new File(".\\saves");
                    	if(saves.exists() == false){
                    		saves.mkdir();
                    	}
                    	File acount = new File(".\\saves\\acount");
                    	if(acount.exists() == false){
                    		acount.mkdir();
                    	}
                    	File ac1 = new File(".\\saves\\acount\\"+Opname);
                    	if(ac1.exists() == false){
                    		ac1.mkdir();
                    	}

                   // 	FileInputStream fis = null;
                    	FileOutputStream fos = null;
                    	DataOutputStream dos = null;
                    	KataokaStation.acount[kal].outputDat(kal);
                    	try{

                    		fos = new FileOutputStream(".\\saves\\acount\\list.dat");
                    		dos = new DataOutputStream(fos);
                    		dos.writeUTF(KataokaStation.list);
                    		fos.close();
                    		dos.close();
                    	}catch(Exception ee){
                    		System.out.println(ee + "認証できませんでした");
                    	}
                    	try{
                    		fos = new FileOutputStream(".\\saves\\acount\\last.dat");
                    		dos = new DataOutputStream(fos);
                    		dos.writeInt(kal);
                    		fos.close();
                    		dos.close();
                    	}catch(Exception ee){
                    		System.out.println(ee + "認証できませんでした");
                    	}
                    	nom.setText("");
                    	nom.setSelectionEnd(nom.getText().length());

                    	acountpnl.removeAll();
                    	acountpnl.setLayout(null);
                		acountpnl.setSize(160,180);
                		acountlist = new JLabel[KataokaStation.acosu];
                    	for(int i = 0;i < KataokaStation.acosu;i = i + 1){
                			acountlist[i] = new JLabel(KataokaStation.acount[i].name);
                			acountlist[i].setBounds(0,i*20,200,20);
                			acountlist[i].setOpaque(true);
                			acountlist[i].setBackground(Color.WHITE);
                			acountlist[i].addMouseListener(new RightBtn(i));
                			acountpnl.add(acountlist[i]);
                		}
                    }


                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    nom.setText("間違えています");
                }else{
                    te.printStackTrace();
                }
            }
		}
	}

	public class RightBtn implements MouseListener{
		public RightBtn(){

		}
		int i3;
		public RightBtn(int i){
			i3 = i;
		}
		 public void mouseReleased(MouseEvent e){

		 }
		 public void mouseClicked(MouseEvent e){
		//	 if (e.isPopupTrigger()) {
					// �|�b�v�A�b�v���j���[��\������
					JComponent c = (JComponent)e.getSource();
					showPopup(c, e.getX(), e.getY());
					e.consume();
		//		}
		 }
		 private void showPopup(JComponent c, int x, int y) {
			 JPopupMenu pmenu = new JPopupMenu();


			 JMenuItem delete = new JMenuItem("認証解除");
			 delete.setAction(new deleteAction(i3));
			 JMenuItem sort = new JMenuItem("並び替え(そろそろ実装します;)");
			 sort.setAction(new sortAction(i3));

			 pmenu.add(sort);
			 pmenu.add(delete);

			 pmenu.show(c, x, y);	// �|�b�v�A�b�v���j���[�̕\��
		 }

		 public class deleteAction extends AbstractAction{
				private static final long serialVersionUID = 1L;
				int i5;
				public deleteAction(int i) {
					super("認証解除");		//���j���[�ɕ\������閼�O
					i5 = i;
				}

				public void actionPerformed(ActionEvent e){
					/*�����ɔF�؉�������*/
					File fil = new File(".\\saves\\acount\\"+KataokaStation.acount[i5].name + "\\data.dat");
					if(fil.delete()){
						System.out.println("kesi1");
						}else {System.out.println("kesenakatta");}
					File fil2 = new File(".\\saves\\acount\\"+KataokaStation.acount[i5].name + "\\order.dat");
					if(fil2.delete()){
						System.out.println("kesi1");
						}else {System.out.println("kesenakatta");}
					File fil3 = new File(".\\saves\\acount\\"+KataokaStation.acount[i5].name);
					if(fil3.delete()){
						System.out.println("kesi1");
						}else {System.out.println("kesenakatta");}
					for(int i = i5;i < KataokaStation.acosu-1;i = i + 1){
						KataokaStation.acount[i] = KataokaStation.acount[i + 1];
						try{
							FileOutputStream fos = new FileOutputStream(".\\saves\\acount\\" + KataokaStation.acount[i].name + "\\order.dat");
				            DataOutputStream dos = new DataOutputStream(fos);
				            dos.writeInt(i);
				            fos.close();
				            dos.close();
						}catch(Exception ee){

						}
					}
					Acount ac[] = new Acount[KataokaStation.acosu - 1];
                	System.arraycopy(KataokaStation.acount, 0, ac, 0, KataokaStation.acosu -1);
                	KataokaStation.acount = ac;
                	if(KataokaStation.acountSel == KataokaStation.acosu-1){
                		KataokaStation.acountSel = KataokaStation.acountSel - 1;
                	}
                	KataokaStation.acosu = KataokaStation.acosu - 1;
                	acountpnl.removeAll();
                	acountpnl.setLayout(null);
            		acountpnl.setSize(160,180);
                	for(int i = 0;i < KataokaStation.acosu;i = i + 1){
            			acountlist[i] = new JLabel(KataokaStation.acount[i].name);
            			acountlist[i].setBounds(0,i*20,200,20);
            			acountlist[i].setOpaque(true);
            			acountlist[i].setBackground(Color.WHITE);
            			acountlist[i].addMouseListener(new RightBtn(i));
            			acountpnl.add(acountlist[i]);
            		}
                	try{
    					FileOutputStream fos = null;
    		        	fos = new FileOutputStream(".\\saves\\acount\\last.dat");
    		        	DataOutputStream dos = new DataOutputStream(fos);
    		        	dos.writeInt(KataokaStation.acountSel);
    		        	fos.close();
    		        	dos.close();
    				}catch(Exception ee){
    					System.out.println(e);
    				}

                	if(KataokaStation.acosu != 0){
                		KataokaStation.frm.setTitle("@" +KataokaStation.acount[KataokaStation.acountSel].name);
                	}else{
                		KataokaStation.frm.setTitle("KUSAMOCHi -N- Ver." + KataokaStation.version);
                	}

                	KataokaStation.list = "";
                	//	if(acosu == 0)list = ".";
                		for(int i = 0;i < KataokaStation.acosu;i = i + 1){
                			if(i == 0){
                				KataokaStation.list = (KataokaStation.list + KataokaStation.acount[i].name);
                			}else{
                				KataokaStation.list = (KataokaStation.list + "\n" + KataokaStation.acount[i].name);
                			}
                		}
                		try{
                        	FileOutputStream fos = null;
                        	fos = new FileOutputStream(".\\saves\\acount\\list.dat");
                        	DataOutputStream dos = new DataOutputStream(fos);
                        	dos.writeUTF(KataokaStation.list);
                        	fos.close();
                        	dos.close();
                		}catch(Exception ee){

                		}
				}
		 }
		 public class sortAction extends AbstractAction{
				private static final long serialVersionUID = 1L;
				int i5;
				public sortAction(int i) {
					super("並び替え(そろそろ実装します;)");		//���j���[�ɕ\������閼�O
					i5 = i;
				}

				public void actionPerformed(ActionEvent e){
					System.out.println("���ёւ�");
					/*�����ɕ��ёւ�����*/
				}
		 }
		 public void mouseExited(MouseEvent e){

		 }
		 public void mouseEntered(MouseEvent e){

		 }
		 public void mousePressed(MouseEvent e){

		 }
	}


	public class finBtn implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Acount.hashu = hashuT.getText();
			setVisible(false);

			File saves = new File(".\\saves");
			if(saves.exists() == false){
			saves.mkdir();
			}

            try{
            FileOutputStream fos = new FileOutputStream(".\\saves\\tag.dat");
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF(Acount.hashu);
            fos.close();
            dos.close();
            }catch(Exception ee){}
		}
	}
}

class Acount{
	String name;
	String token;
	String secret;
	static String hashu = "";

	public Acount(){
		name = "No Name";
		token = "";
		secret = "";
	}

	public void setName(String str){
		name = str;
	}
	public void setToken(String str){
		token = str;
	}
	public void setSecret(String str){
		secret = str;
	}

	public void outputDat(int i){
		String str;
		str = name + "\n" + token + "\n" + secret;
		File saves = new File(".\\saves");
    	if(saves.exists() == false){
    		saves.mkdir();
    	}
    	File acount = new File(".\\saves\\acount");
    	if(acount.exists() == false){
    		acount.mkdir();
    	}
    	File ac1 = new File(".\\saves\\acount\\"+name);
    	if(ac1.exists() == false){
    		ac1.mkdir();
    	}
		try{
			FileOutputStream fos = new FileOutputStream(".\\saves\\acount\\"+name+"\\data.dat");
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeUTF(str);
			fos.close();
			dos.close();
			fos = new FileOutputStream(".\\saves\\acount\\"+name+"\\order.dat");
			dos = new DataOutputStream(fos);
			dos.writeInt(i);
			fos.close();
			dos.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public void inputDat(String acountstr){
		String strary[];
		String str = "";
		try{
			FileInputStream fis = new FileInputStream(".\\saves\\acount\\"+acountstr+"\\data.dat");
			DataInputStream dis = new DataInputStream(fis);
			str = dis.readUTF();
			fis.close();
			dis.close();
		}catch(Exception e){
			System.out.println(e);
		}
		strary = str.split("\n");
		System.out.println(str + acountstr);
		name = strary[0];
		token = strary[1];
		secret = strary[2];
	/*	int i = 0;
		try{
			FileInputStream fis = new FileInputStream("./saves/acount/"+name+"/order.dat");
			DataInputStream dis = new DataInputStream(fis);
			i = dis.readInt();
			fis.close();
			dis.close();
		}catch(Exception e){
		}
		return i;*/
	}
	static public int inputOrder(String namestr){
		int i = 0;
		try{
			FileInputStream fis = new FileInputStream(".\\saves\\acount\\"+namestr+"\\order.dat");
			DataInputStream dis = new DataInputStream(fis);
			i = dis.readInt();
			fis.close();
			dis.close();
		}catch(Exception e){
		}
		return i;
	}
	static public void outputList(){

	}
	static public void outputLast(){

	}

}
