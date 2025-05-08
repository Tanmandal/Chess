class Square
{
    Piece pce;int cl,x,y;//1w0b
    Square(int xx,int yy) 
    {
        pce=new Piece();
        cl=(xx+yy+1)%2;
        x=xx;
        y=yy;
    }
    void set(int p,int c)
    {
        pce=new Piece(p,c);
    }
    void transfer(Piece p)
    {
        pce.pc=p.pc;
        pce.col=p.col;
    }
    void transfer()
    {
        pce.pc=0;
        pce.col=0;
    }
    boolean isEmpty()
    {
        return pce.pc==0;
    }
}