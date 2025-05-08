class Piece
{
    int pc,col;
    Piece(int p,int c)
    {
        pc=p;
        col=c;
    }
    Piece()
    {
        pc=0;
        col=0;
    }
    boolean inside(int x,int y)
    {
        return !(x<0 || x>7 || y<0 || y>7);
    }
    boolean match(int x,int y,int w,int v)
    {
        return (inside(w,v) && w==x && v==y);
    }
    boolean mypiece(Board b,int x,int y,int nx,int ny)
    {
        return b.sq[x][y].pce.col==b.sq[nx][ny].pce.col;
    }
    boolean king(Board b,int x,int y,int nx,int ny)
    {
        if(mypiece(b,x,y,nx,ny))
         return false;
        if((x==nx+1 || x==nx-1) && y==ny)
         return true;
        if(x==nx && (y==ny+1 || y==ny-1))
         return true;
        if((x==nx+1 || x==nx-1) && (y==ny+1 || y==ny-1))
         return true;
        return false;
    }
    boolean queen(Board b,int x,int y,int nx,int ny)
    {
        return (rook(b,x,y,nx,ny)||bishop(b,x,y,nx,ny));
    }
    boolean pawn(Board b,int x,int y,int nx,int ny)
    {
        if(nx==x+((b.sq[x][y].pce.col==2)?1:-1) && ny==y && b.sq[nx][ny].isEmpty())
         return true;
        if(nx==x+((b.sq[x][y].pce.col==2)?2:-2) && ny==y && b.sq[nx][ny].isEmpty()&& b.sq[nx-((b.sq[x][y].pce.col==2)?1:-1)][ny].isEmpty() && x==((b.sq[x][y].pce.col==2)?1:6))
         return true;
        if(nx==x+((b.sq[x][y].pce.col==2)?1:-1) && (ny==y-1 || ny==y+1) && (!b.sq[nx][ny].isEmpty()) && (!mypiece(b,x,y,nx,ny)))
         return true;
        return false;
    }
    boolean knight(Board b,int x,int y,int nx,int ny)
    {
        int w=nx+2;
        int v=ny+1;
        if(match(x,y,w,v))
         return !mypiece(b,x,y,nx,ny);
        v=ny-1;
        if(match(x,y,w,v))
         return !mypiece(b,x,y,nx,ny);
        w=nx-2;
        if(match(x,y,w,v))
         return !mypiece(b,x,y,nx,ny);
        v=ny+1;
        if(match(x,y,w,v))
         return !mypiece(b,x,y,nx,ny);
        w=nx+1;
        v=ny+2;
        if(match(x,y,w,v))
         return !mypiece(b,x,y,nx,ny);
        w=nx-1;
        if(match(x,y,w,v))
         return !mypiece(b,x,y,nx,ny);
        v=ny-2;
        if(match(x,y,w,v))
         return !mypiece(b,x,y,nx,ny);
        w=nx+1;
        if(match(x,y,w,v))
         return !mypiece(b,x,y,nx,ny);
        return false;
    }
    boolean rook(Board b,int x,int y,int nx,int ny)
    {
        if(mypiece(b,x,y,nx,ny))
         return false;
        int fl=0;
        if(x==nx)
         fl=1;
        else if(y==ny)
         fl=2;
        else
         return false;
        if(fl==1)
        {
            for(int i=Math.min(y,ny)+1;i<Math.max(y,ny);i++)
             if(!b.sq[x][i].isEmpty())
              return false;
        }
        else if(fl==2)
        {
            for(int i=Math.min(x,nx)+1;i<Math.max(x,nx);i++)
             if(!b.sq[i][y].isEmpty())
              return false;
        }
        return true;
    }
    boolean bishop(Board b,int x,int y,int nx,int ny)
    {
        if(mypiece(b,x,y,nx,ny) || Math.abs(x-nx)!=Math.abs(y-ny))
         return false;
        int sx=(int)Math.signum(nx-x),sy=(int)Math.signum(ny-y);
        for(int i=1;i<Math.abs(x-nx);i++)
         if(!b.sq[x+i*sx][y+i*sy].isEmpty())
           return false;
        return true;
    }
    boolean validate(Board b,int x,int y,int nx,int ny)
    {
        if(!(inside(x,y) && inside (nx,ny)))
         return false;
        int pi=b.sq[x][y].pce.pc;
        switch(pi)
        {
            case 1:return king(b,x,y,nx,ny);
            case 2:return queen(b,x,y,nx,ny);
            case 3:return bishop(b,x,y,nx,ny);
            case 4:return knight(b,x,y,nx,ny);
            case 5:return rook(b,x,y,nx,ny);
            case 6:return pawn(b,x,y,nx,ny);
            default :return false;
        }
    }
}