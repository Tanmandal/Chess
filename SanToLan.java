import java.util.*;
import java.io.*;

class SanToLan
{
    void main()
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter valid Fen for match with non-standard Starting Position");
        String fen=sc.nextLine();
        Board b=new Board();
        String nt=new Notation().FentoBoard(fen,b);
        if(nt.indexOf("Invalid Fen")>-1)
        {
             b=new Board();
             b.setup();
             fen="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        }
        System.out.println("Enter San");
        String san=sc.nextLine();
        san=removeExtras(san);
        StringTokenizer s1=new StringTokenizer(san);
        String t="";
        int cr=1;
        while(s1.hasMoreTokens())
        {
            String m=s1.nextToken();
            m=m.trim();
            if(isNumber(m))
              continue;
            String p="",nm="";
            if(m.indexOf('=')!=-1)
            {
                p=""+m.charAt(m.length()-1);
                m=m.substring(0,m.length()-2);
            }
            if(m.equals("0-0")||m.equals("O-O"))
            {
                if(cr==1)
                 nm="e1g1";
                else
                 nm="e8g8";
            }
            else if(m.equals("0-0-0")||m.equals("O-O-O"))
            {
                if(cr==1)
                 nm="e1c1";
                else
                 nm="e8c8";
            }
            else
            {
                String am=m.substring(m.length()-2);
                int pt=piece(m);
                if(pt!=6)
                 m=m.substring(1);
                String st=m.substring(0,m.length()-2);
                String mvs=b.humoves(cr,false),mk="";
                StringTokenizer s2=new StringTokenizer(mvs);
                int fl=0;
                while(s2.hasMoreTokens())
                {
                    mk=s2.nextToken();
                    if(mk.substring(2).equals(am))
                    {
                        int x=8-mk.charAt(1)+48;
                        int y=mk.charAt(0)-97;
                        if(b.sq[x][y].pce.pc==pt)
                        {
                            if(st.length()==0)
                              {fl=1;break;}
                            else if(mk.substring(0,2).indexOf(st)!=-1)
                              {fl=1;break;}
                        }
                    }
                }
                if(fl==1)
                 nm=mk+p;
                else
                 {System.out.println("Invalid");System.exit(0);}
            }
            t=t+"\n"+nm;
            b.move(nm.substring(0,2),nm.substring(2,4),cr,"qbnr".indexOf(p)+1);
            cr=cr%2+1;
        }
        System.out.println("1.Display\n2.Create LAN file");
        System.out.println("Enter Choice");
        int ch=sc.nextInt();
        String fnm="";
        switch(ch)
        {
            case 1:System.out.println(t);break;
            case 2:System.out.println("Enter File Name");
                   fnm=sc.next();
                   try
                   {
                       FileWriter fw=new FileWriter(fnm+".txt");
                       PrintWriter pw=new PrintWriter(fw);
                       pw.println(fen+t);
                       pw.close();
                   }catch(Exception e){}
                   break;
            case 3:System.out.println("Invalid Choice");
        }
        
    }
    int piece(String m)
    {
        String p="KQBNR";
        if(p.indexOf(m.charAt(0))==-1)
          return 6;
        else
          return p.indexOf(m.charAt(0))+1;
    }
    boolean isNumber(String s)
    {
        if(s.length()==0)
          return true;
        if(!Character.isDigit(s.charAt(0)))
          return false;
        return isNumber(s.substring(1));
    }
    String removeExtras(String san)
    {
        int i=san.indexOf('x');
        int j=san.indexOf('+');
        int k=san.indexOf('.');
        int l=san.indexOf('#');
        if(i!=-1)
          san=san.substring(0,i)+san.substring(i+1);
        else if(j!=-1)
          san=san.substring(0,j)+san.substring(j+1);
        else if(k!=-1)
          san=san.substring(0,k)+" "+san.substring(k+1);
        else if(l!=-1)
          san=san.substring(0,l)+san.substring(l+1);
        else
          return san;
        return removeExtras(san);
    }
}