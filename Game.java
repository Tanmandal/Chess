import java.util.*;
import java.nio.file.*;
import java.io.*;
class Game
{
    static String a="",b="",n[]=new String[2],mth="";static int pro=0,pch[]=new int[2];static boolean rot=false;
    static void playername()
    {
        Scanner sc=new Scanner(System.in);
        for(int i=1;i<=2;i++)
        {
          System.out.println(i==1?"White":"Black");
          System.out.println("1.Stockfish\n2.Komodo\n3.Leela\n4.Gull\n5.Human\n6.Exit");
          System.out.println("Enter Choice");
          pch[i-1]=sc.nextInt();
          switch(pch[i-1])
          {
              case 1:n[i-1]="Stockfish";break;
              case 2:n[i-1]="Komodo";break;
              case 3:n[i-1]="Leela";break;
              case 4:n[i-1]="Gull";break;
              case 5:System.out.println("Enter Name of Player");
                     n[i-1]=sc.next();break;
              case 6:System.exit(0);
              default:System.out.println("Invalid Choice");i--;
          }
        }
    }
    static String minFen(Board br)
    {
        Notation nt=new Notation();
        String kb=(nt.toFEN(br,0,0));
        kb=kb.substring(0,kb.indexOf(' '));
        return kb;
    }
    public static void main(String args[])
    {
        Scanner sc=new Scanner(System.in);
        playername();
        Board br=new Board(); 
        int c=0,ctr=0;
        System.out.println("Enter Valid Fen to Start match from Position");
        String fen=sc.nextLine();
        String nt=new Notation().FentoBoard(fen,br);
        String positions="";
        if(nt.indexOf("Invalid Fen")>-1)
         {
             br=new Board();
             br.setup();
         }
        else
        {
            c=nt.charAt(0)-49;
            ctr=2*Integer.valueOf(nt.substring(nt.indexOf(" ")+1))+c-1;
            //System.out.println(ctr+"\t"+c);
            br.display();
        }
        if(pch[0]==5 && pch[1]==5)
         rot=c%2==0;
        else if(pch[0]==5 || pch[1]==5)
         rot=pch[0]==5;
        else
         rot=true;
        
        Gui g=new Gui();
        g.gui(br,rot);
        //String a="",b="";
        //RandomMove ran=new RandomMove();
        System.out.println("Do you want to save game?");
        String sv=sc.next();
        if(sv.toLowerCase().equals("yes"))
        {
            System.out.println("Save As");
            mth=sc.next();
            record(new Notation().toFEN(br,c,ctr/2+1),false,0);
        }
        positions=minFen(br);
        while(true)
        {
            pro=0;
            c=c%2+1;
            play(br,c,ctr);
            //System.out.println(n.toFEN(br,c-1,ctr/2+1));
            
            //player(n2);
            int x=8-(a.toUpperCase()).charAt(1)+48;
            int y=(a.toUpperCase()).charAt(0)-65;
            int nx=8-(b.toUpperCase()).charAt(1)+48;
            int ny=(b.toUpperCase()).charAt(0)-65;
            boolean cng=br.sq[x][y].pce.pc==6 || br.sq[nx][ny].pce.col==c%2+1;
            if(br.move(a,b,c,pro))
            {
                ctr++;
                if(sv.toLowerCase().equals("yes"))
                  record(a+b,true,br.promote);
                br.promote=0;
                g.dis();
                g.gui(br,rot);
                int mt=br.mate(c%2+1);
                if(mt==1)
                {
                    System.out.println("Checkmate");
                    if(c==1)
                      System.out.println(n[0]+" wins");
                    else
                      System.out.println(n[1]+" wins");
                    break;
                }
                else if(mt==2)
                {
                    System.out.println("Stalemate");
                    break;
                }
                String nFen=minFen(br);
                if(thrfldrep(positions,nFen))
                {
                    System.out.println("Draw");
                    break;
                }
                if(cng)
                  positions=nFen;
                else
                  positions+="\t"+nFen;
                //ran.delay();
            }
            else
            {
                System.out.println("Invalid Move");
                rotate();
                c--;
                //g.gui(br);
                //System.out.println(a+" "+b);
                //break;
            }
        }
    }
    static boolean thrfldrep(String positions,String nFen)
    {
        int c=0;
        StringTokenizer st=new StringTokenizer(positions,"\t");
        while(st.hasMoreTokens())
        {
            String w=st.nextToken();
            if(w.equals(nFen))
             c++;
            if(c==2)
             return true;
        }
        return false;
    }
    static void play(Board br,int c,int ctr)
    {
        if(pch[c-1]==5)
              human(n[c-1]);
            else
              bot(br,c-1,ctr/2+1,pch[c-1]);
        rotate();
    }
    static void rotate()
    {
        if(pch[0]==5 && pch[1]==5)
         rot=!rot;
    }
    static void human(String n)
    {
        Scanner sc=new Scanner(System.in);
        System.out.println(n+"\nPiece");
        a=sc.next();
        //a=ran.generate(); 
        //System.out.println(a);
        System.out.println("Move to");
        b=sc.next();
        //b=ran.generate();
        //System.out.println(b);
    }
    static void bot(Board br,int pl,int mn,int p)
    {
        String pp="";
        switch(p)
        {
            case 1:pp="BOTS/stockfish/stockfish";break;
            case 2:pp="BOTS/komodo/komodo";break;
            case 3:pp="BOTS/lc0/lc0";break;
            case 4:pp="BOTS/Gull/LazyGull";break;
        }
        Bot bt=new Bot(pp);
        Notation n=new Notation();
        //System.out.println(n.toFEN(br,pl,mn));
        String k=bt.getMove(n.toFEN(br,pl,mn));
        System.out.println(k);
        a=k.substring(0,2);
        b=k.substring(2,4);
        switch(k.charAt(4))
        {
            case 'q':pro=1;break;
            case 'b':pro=2;break;
            case 'n':pro=3;break;
            case 'r':pro=4;break;
            default:pro=0;
        }
        bt.stopEngine();
    }
    static void record(String k,boolean bol,int prom)
    {
         String pr="";
         switch(prom)
         {
             case 1:pr="q";break;
             case 2:pr="b";break;
             case 3:pr="n";break;
             case 4:pr="r";break;
         }
         try
          {
           FileWriter fw=new FileWriter(mth+".txt",bol);
           PrintWriter pw=new PrintWriter(fw);
           pw.println(k+pr);
           pw.close();
          }
          catch(Exception e){}
         
    }
}