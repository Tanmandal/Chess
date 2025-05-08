import java.util.*;
import java.util.*;
import java.io.*;
class Analyse
{
    int choice(String mv)
    {
        if(mv.length()<5)
         return 0;
        int c=0;
        switch(mv.charAt(4))
        {
            case 'q':c=1;break;
            case 'b':c=2;break;
            case 'n':c=3;break;
            case 'r':c=4;break;
        }
        return c;
    }
    Pgn read(Pgn start,String f)
    {//#Reads record from file
        Pgn end=new Pgn();
        try
        {
            FileReader fr=new FileReader(f+".txt");
            BufferedReader bf=new BufferedReader(fr);
            String s="";int fl=0,c=0;
            while((s=bf.readLine())!=null)
            {
                if(s.length()==0)
                 break;
                if(fl==0)
                {
                    c=start.begin(s);
                    end=start;
                    fl=1;
                }
                else
                {
                    if(!end.add(s,c,choice(s)))
                    {
                        System.out.println(s);
                        System.out.println("Invalid Move");
                        System.exit(0);
                    }
                    end=end.nxt;c=c%2+1;
                }
                
            }
            bf.close();
        }
        catch(Exception e)
        {
            System.out.println("This Game Does Not Exists"+e);
            System.exit(0);
        }
        return end;
    }
    void cmd(String c)
    {
        try
        {
            new ProcessBuilder("cmd","/c",c).inheritIO().start().waitFor();
        }
        catch(Exception e)
        {
            System.out.println("5ome 3rror 0curr3d");
        }
    }
    void main()
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter File Name");
        String f=sc.nextLine();
        Pgn start=new Pgn();
        Pgn end=read(start,f);
        cmd("cls");
        System.out.println("1.Manual\n2.Auto\nEnter Your Choice");
        int ch=sc.nextInt();
        if(ch==1)
         {System.out.println(f);manual(start,end);}
        else if(ch==2)
         {System.out.println(f);play(start);}
        else
        {
            System.out.println("Invalid Choice");
            System.out.println();
        }
    }
    void play(Pgn start)
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Delay in seconds");
        double r=sc.nextDouble();
        System.out.println("Select Side\n1.White\n2.Black");
        int c=sc.nextInt();boolean rot=true;
        if(c==2)
         rot=false;
        Gui g=new Gui();
        Pgn tmp=start;
        while(tmp!=null)
        {
            g.gui(tmp.br,rot);
            delay((long)(r*1000));
            if(tmp.nxt==null)
             cmd("pause");
            g.dis();
            tmp=tmp.nxt;
        }
        
    }
    void manual(Pgn start,Pgn end)
    {
        Scanner sc=new Scanner(System.in);
        Gui g=new Gui();
        Pgn tmp=start;
        String u="";boolean rot=true;
        while(!u.equals("exit"))
        {
            g.gui(tmp.br,rot);
            if(tmp.turn==1)
             System.out.println("White "+tmp.move);
            else if(tmp.turn==2)
             System.out.println("Black "+tmp.move);
            u=sc.nextLine();
            if(u.equals("p")&& tmp.prv!=null)
             tmp=tmp.prv;
            else if(u.equals("n") && tmp.nxt!=null)
             tmp=tmp.nxt;
            else if(u.equals("start"))
             tmp=start;
            else if(u.equals("end"))
             tmp=end;
            else if(u.equals("rotate"))
             rot=!rot;
            else if(u.equals("stop"))
             break;
            g.dis();
        }
    }
    void delay(long r)
    {
      try
      {
        Thread.sleep(r);   
      }
      catch(InterruptedException ex)
      {
          ex.printStackTrace();
      }
    }
}