class Notation
{
    
    String toFEN(Board b,int pl,int mn)
    {
        String t="";
        for(int i=0;i<8;i++)
        {
            int c=0;
            for(int j=0;j<8;j++)
            {
                if(b.sq[i][j].isEmpty())
                 c++;
                else
                {
                    if(c>0)
                     t=t+c;
                    c=0;
                    char p=' ';
                    switch(b.sq[i][j].pce.pc)
                    {
                        case 1:p='k';break;
                        case 2:p='q';break;
                        case 3:p='b';break;
                        case 4:p='n';break;
                        case 5:p='r';break;
                        case 6:p='p';break;
                    }
                    if(b.sq[i][j].pce.col==1)
                     p=Character.toUpperCase(p);
                    t=t+p;
                }
                
            }
            t=t+((c>0)?c:"")+"/";
        }
        t=t.substring(0,t.length()-1);
        t=t+" "+((pl==0)?"w ":"b ")+((b.chk.length()==0)?"-":b.chk)+EnPassant(b)+b.hm+" "+mn;
        return t;
    }
    String EnPassant(Board b)
    {
        if(b.rmv.length()==0)
         return " - ";
        if(b.sq[b.rmv.charAt(2)-48][b.rmv.charAt(3)-48].pce.pc!=6)
         return " - ";
        if(b.rmv.charAt(1)!=b.rmv.charAt(3))
         return " - ";
        if((b.rmv.charAt(0)=='1' && b.rmv.charAt(2)=='3')||(b.rmv.charAt(0)=='6' && b.rmv.charAt(2)=='4'))
         return " "+(char)(b.rmv.charAt(3)-48+97)+(8-(b.rmv.charAt(2)+b.rmv.charAt(0))/2+48)+" ";
        else
         return " - ";
    }
    String FentoBoard(String fen,Board b)
    {
        
        try{
        fen=fen.trim();
        String ret="";
        //Board b=new Board();
        int i=0,j=0;boolean fl1=false,fl2=false;
        for(int k=0;k<fen.indexOf(" ");k++)
        {
            char ch=fen.charAt(k);
            if(ch=='K')
            {
                if(fl1)
                {
                    return "Invalid Fen1";
                }
                fl1=true;
            }
            if(ch=='k')
            {
                if(fl2)
                {
                    return "Invalid Fen2";
                }
                fl2=true;
            }
            if(Character.isDigit(ch) && ch!='0')
             j=j+ch-48;
            else if(Character.isLetter(ch))
            {
                switch(Character.toLowerCase(ch))
                {
                    case 'k':b.sq[i][j].pce.pc=1;break;
                    case 'q':b.sq[i][j].pce.pc=2;break;
                    case 'b':b.sq[i][j].pce.pc=3;break;
                    case 'n':b.sq[i][j].pce.pc=4;break;
                    case 'r':b.sq[i][j].pce.pc=5;break;
                    case 'p':b.sq[i][j].pce.pc=6;break;
                    default:return "Invalid Fen3";
                }
                if(Character.isUpperCase(ch))
                 b.sq[i][j].pce.col=1;
                else
                 b.sq[i][j].pce.col=2;
                j++;
            }
            else if(ch=='/' && j==8)
            {
                i++;
                j=0;
            }
            else
            {
                return "Invalid Fen4";
            }
        }
        //rnbqkbnr/pppp2pp/4p3/5pB1/2P4P/3P4/PP2PPP1/RN1QKBNR b KQkq c3 0 1
        fen=fen.substring(fen.indexOf(' '));
        if(fen.charAt(1)=='b')
         ret="2";
        else if(fen.charAt(1)=='w')
         ret="1";
        else
         return "Invalid Fen5";
        fen=fen.trim().substring(fen.indexOf(' '));fen=fen.trim().substring(fen.indexOf(' '));
        fen=fen.trim();
        String temp=fen.substring(0,fen.indexOf(" "));
        if(existsinorder(temp,"KQkq"))
         b.chk=temp;
        else if(temp.equals("-"))
         b.chk="";
        else
         return "Invalid Fen6";
        fen=fen.trim().substring(fen.indexOf(' '));
        fen=fen.trim();
        temp=fen.substring(0,fen.indexOf(" "));
        if(temp.equals("-"))
         b.rmv="";
        else if(temp.length()==2)
        {
            //System.out.println(temp.charAt(0)-97-1);
            if(temp.charAt(1)-48==3 && temp.charAt(0)>='a' && temp.charAt(0)<='h')
            {
                if(b.sq[8-temp.charAt(1)+48][temp.charAt(0)-97].pce.col==0 && b.sq[8-temp.charAt(1)+48-1][temp.charAt(0)-97].pce.col==1)
                 b.rmv=""+(8-temp.charAt(1)+48+1)+(temp.charAt(0)-97)+(8-temp.charAt(1)+48-1)+(temp.charAt(0)-97);
                else
                 return "Invalid Fen7";
            }
            else if(temp.charAt(1)-48==6 && temp.charAt(0)>='a' && temp.charAt(0)<='h')
            {
                if(b.sq[8-temp.charAt(1)+48][temp.charAt(0)-97].pce.col==0 && b.sq[8-temp.charAt(1)+48+1][temp.charAt(0)-97].pce.col==2)
                 b.rmv=""+(8-temp.charAt(1)+48-1)+(temp.charAt(0)-97)+(8-temp.charAt(1)+48+1)+(temp.charAt(0)-97);
                else
                 return "Invalid Fen8";
            }
            else 
             return "Invalid Fen9";
        }
        else
         return "Invalid Fen10";
        fen=fen.trim().substring(fen.indexOf(' '));
        fen=fen.trim();
        int sp=0;boolean num=false;
        for(int z=0;z<fen.length();z++)
        {
            char ch=fen.charAt(z);
            if(ch==' ' && num)
            {
                num=false;
                sp++;
            }
            else if(Character.isDigit(ch))
             num=true;
            else
             return "Invalid Fen11";

        }
        if(sp!=1 || !num)
         return "Invalid Fen12";
        else
        {
            b.hm=Integer.valueOf(fen.substring(0,fen.indexOf(" ")));
            if(b.hm!=0 && b.rmv.length()!=0)
             return "Invalid Fen13";
            ret=ret+fen.substring(fen.indexOf(" "));
        }
        return ret;
       }catch(Exception e)
       {
           return "Invalid Fen14";
       }
       
    }
    boolean existsinorder(String t,String s)
    {
        int i=0;
        for(int j=0;i<t.length() && j<s.length();)
        {
            for(;j<s.length();j++)
             if(s.charAt(j)==t.charAt(i))
              {i++;break;}
              
        }
        return i==t.length();
    }
}