import java.util.*;
class Board
{
    Square sq[][];String chk="",rmv="";int hm,promote=0;
    Board()
    {
        sq=new Square[8][8];
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
             sq[i][j]=new Square(i,j);
        }
        chk="KQkq";
        rmv="";
        hm=0;
    }
    public static void cls()
    {
        try
        {
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        }
        catch(Exception e)
        {
            System.out.println("5ome 3rror 0curr3d");
        }
    }
    boolean inside(int x,int y)
    {
        return !(x<0 || x>7 || y<0 || y>7);
    }
    boolean  EnPassant(int x,int y,int nx,int ny,int cr)
    {
        
        if(!(inside(x,y) && inside (nx,ny)))
          return false;
          
        if(!(sq[x][y].pce.pc==6 && sq[x][y].pce.col==cr))
          return false;
          
        int k=0;
        if(cr==1)
         k=1;
        else if(cr==2)
         k=-1;
        if(!((x==3 || x==4) && (ny==y+1 || ny==y-1) && nx==x-k))
          return false;          
       
        if(!(sq[nx][ny].isEmpty() && rmv.equals(""+(x-2*k)+ny+x+ny)))
         return false;
         
        Board nbr=new Board();
        copy(nbr);
        nbr.sq[nx][ny].transfer(nbr.sq[x][y].pce);
        nbr.sq[x][y].transfer();
        nbr.sq[x][ny].transfer();
        if(nbr.check(cr))
          return false;
          
        nbr.copy(this);
        this.chk=castleRight();
        rmv=""+x+y+nx+ny;
        return true;
    }
    boolean castle(int x,int y,int nx,int ny,int cr)
    { 
         if(!(inside(x,y) && inside (nx,ny)))
          return false;
         
         if(!((x==0 || x==7) && y==4 && nx==x && (ny==2 || ny==6)))
          return false;
         
         if(!((cr==1 && x==7 && (chk.indexOf("K")!=-1 || chk.indexOf("Q")!=-1)) || (cr==2 && x==0 && (chk.indexOf("k")!=-1 || chk.indexOf("q")!=-1))))
          return false;
         
         int k=(int)Math.signum(ny-y);
         if(!((k==-1 && (chk.indexOf("q")!=-1 || chk.indexOf("Q")!=-1))||(k==1 && (chk.indexOf("k")!=-1 || chk.indexOf("K")!=-1))))
          return false;
         
         Board nbr=new Board();
         copy(nbr);
         if(nbr.check(cr))
          return false;
          
         copy(nbr);
         if(!nbr.move(x,y,x,y+k,cr,false))
          return false;
         
         if(!nbr.move(x,y+k,x,y+2*k,cr,false))
          return false;
          
         copy(nbr);
         nbr.sq[nx][ny].transfer(nbr.sq[x][y].pce);
         nbr.sq[x][y].transfer();
         nbr.sq[x][y+k].transfer(nbr.sq[x][((k==-1)?0:7)].pce);
         nbr.sq[x][((k==-1)?0:7)].transfer();
         if(nbr.check(cr))
          return false;
          
         nbr.copy(this);
         this.chk=castleRight();
         rmv=""+x+y+nx+ny;
         return true;
    }
    String castleRight()
    {
        String c="";
        if(sq[7][4].pce.pc==1 && sq[7][4].pce.col==1)
        {
            if(sq[7][7].pce.pc==5 && sq[7][7].pce.col==1 && chk.indexOf("K")!=-1)
             c=c+"K";
            if(sq[7][0].pce.pc==5 && sq[7][0].pce.col==1 && chk.indexOf("Q")!=-1)
             c=c+"Q";
        }
        if(sq[0][4].pce.pc==1 && sq[0][4].pce.col==2)
        {
            if(sq[0][7].pce.pc==5 && sq[0][7].pce.col==2 && chk.indexOf("k")!=-1)
             c=c+"k";
            if(sq[0][0].pce.pc==5 && sq[0][0].pce.col==2 && chk.indexOf("q")!=-1)
             c=c+"q";
        }
        return c;
    }
    boolean halfmove(int x,int y,int nx,int ny)
    {
        if(sq[x][y].pce.pc==6)
         return true;
        if(!sq[x][y].isEmpty())
         return true;
        return false;
    }
    boolean move(String a,String b,int cr,int pro)
    {
        a=a.toUpperCase();
        b=b.toUpperCase();
        int x=8-a.charAt(1)+48;
        int y=a.charAt(0)-65;
        int nx=8-b.charAt(1)+48;
        int ny=b.charAt(0)-65;
        if(!(inside(x,y) && inside(nx,ny)))
         return false;
        boolean hb=halfmove(x,y,nx,ny);
        int nhm=hm;
        if(sq[x][y].pce.col==cr)
        {
            if(move(x,y,nx,ny,cr,pro))
            {
                //sq[nx][ny].transfer(sq[x][y].pce);
                //sq[x][y].transfer();
                if(hb)
                 hm=0;
                else
                 hm=nhm+1;;
                cls();
                display();
                return true;
            }
        }
        hm=nhm;
        return false;
    }
    boolean move(int x,int y,int nx,int ny,int cr,int pro)
    {
        Scanner sc=new Scanner(System.in);
        if(sq[x][y].pce.validate(this,x,y,nx,ny))
        {
            Board nbr=new Board();
            copy(nbr);
            nbr.sq[nx][ny].transfer(sq[x][y].pce);
            nbr.sq[x][y].transfer();
            if(nbr.check(cr))
            {
                return false;
            }
            else
            {
                this.sq=nbr.sq;
                this.chk=castleRight();
                rmv=""+x+y+nx+ny;
                if((nx==0 || nx==7)&& sq[nx][ny].pce.pc==6)
                {
                    int cho=0;
                    System.out.println("1.Queen\n2.Bishop\n3.Knight\n4.Rook\nEnter Your Choice");
                    do
                    {
                        if(pro==0)
                          cho=sc.nextInt();
                        else
                          cho=pro;
                        sq[nx][ny].pce.pc=cho+1;
                    }while(checkchoice(cho));
                    promote=cho;
                }
                return true;
            }
        }
        return (castle(x,y,nx,ny,cr)?true:EnPassant(x,y,nx,ny,cr));
    }
    boolean move(int x,int y,int nx,int ny,int cr,boolean pw)
    {
        Scanner sc=new Scanner(System.in);
        if(sq[x][y].pce.validate(this,x,y,nx,ny))
        {
            Board nbr=new Board();
            copy(nbr);
            nbr.sq[nx][ny].transfer(sq[x][y].pce);
            nbr.sq[x][y].transfer();
            if(nbr.check(cr))
            {
                return false;
            }
            else
            {
                this.sq=nbr.sq;
                this.chk=castleRight();
                rmv=""+x+y+nx+ny;
                if((nx==0 || nx==7)&& sq[nx][ny].pce.pc==6 && pw)
                {
                    int cho=0;
                    System.out.println("1.Queen\n2.Bishop\n3.Knight\n4.Rook\nEnter Your Choice");
                    do
                    {
                        cho=sc.nextInt();
                        sq[nx][ny].pce.pc=cho+1;
                    }while(checkchoice(cho));
                    promote=cho;
                }
                return true;
            }
        }
        return (castle(x,y,nx,ny,cr)?true:EnPassant(x,y,nx,ny,cr));
    }
    boolean checkchoice(int cho)
    {
        if(cho<1 || cho>4)
        {
            System.out.println("Invalid Choice\nEnter Again");
            return true;
        }
        else
         return false;
    }
    void copy(Board nbr)
    {
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
             nbr.sq[i][j].transfer(sq[i][j].pce);
        }
        nbr.chk=chk;
        nbr.rmv=rmv;
    }
    void display()
    {
        for(char i='A';i<='H';i++)
        {
            System.out.print(" \t "+i);
        }
        System.out.println("\n");
        for(int i=0;i<8;i++)
        {
            System.out.print(8-i+"\t");
            for(int j=0;j<8;j++)
             System.out.print(sq[i][j].pce.col+"|"+sq[i][j].pce.pc+"\t");
            System.out.println(8-i);
        }
        System.out.println();
        for(char i='A';i<='H';i++)
        {
            System.out.print(" \t "+i);
        }
        System.out.println();
    }
    void setup()
    {
        sq[0][4].set(1,2);
        sq[7][4].set(1,1);
        sq[0][3].set(2,2);
        sq[7][3].set(2,1);
        sq[0][2].set(3,2);sq[0][5].set(3,2);
        sq[7][2].set(3,1);sq[7][5].set(3,1);
        sq[0][1].set(4,2);sq[0][6].set(4,2);
        sq[7][1].set(4,1);sq[7][6].set(4,1);
        sq[0][0].set(5,2);sq[0][7].set(5,2);
        sq[7][0].set(5,1);sq[7][7].set(5,1);
        for(int i=0;i<8;i++)
        {
            sq[1][i].set(6,2);
            sq[6][i].set(6,1);
        }
        display();
    }
    boolean check(int cr)
    {
        Square m=findking(cr);
        Square o=findking(cr%2+1);
        int mc=0,oc=0;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(sq[i][j].pce.validate(this,i,j,m.x,m.y))
                 mc=1;
                if(sq[i][j].pce.validate(this,i,j,o.x,o.y))
                 {
                     sq[i][j].cl=3;
                     oc=1;
                 }
            }
        }
        if(mc==1)
         return true;
        else
        {
            if(oc==1)
             o.cl=2;
            return false;
        }
    }
    Square findking(int cr)
    {
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(sq[i][j].pce.pc==1 && sq[i][j].pce.col==cr)
                 return sq[i][j];
            }
        }
        return new Square(-1,-1);
    }
    int mate(int cr)
    {
        Square m=findking(cr);
        if(moves(cr,true,false).length()==0)
        {
            if(m.cl==2)
             return 1;
            else
             return 2;
        }
        return 0;
    }
    String moves(int cr,boolean brk,boolean all)
    {
        String m="";
        Board nbr=new Board();
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(((!sq[i][j].isEmpty())&& sq[i][j].pce.col==cr)||all)
                for(int ni=0;ni<8;ni++)
                {
                    for(int nj=0;nj<8;nj++)
                    {
                        copy(nbr);
                        if(nbr.move(i,j,ni,nj,cr,false))
                         {
                             m=m+i+j+ni+nj+" ";
                             if(brk)
                              return m;
                         }
                    }
                }
            }
        }
        return m;
    }
    String humoves(int cr,boolean all)
    {
        String m=moves(cr,false,all);
        StringTokenizer s=new StringTokenizer(m);
        String t="";
        while(s.hasMoreTokens())
        {
            String mv=s.nextToken();
            int x=(8-mv.charAt(0)+48);
            char y=(char)(mv.charAt(1)+97-48);
            int nx=(8-mv.charAt(2)+48);
            char ny=(char)(mv.charAt(3)+97-48);
            t=t+y+x+ny+nx+" ";
        }
        return t;
    }
}