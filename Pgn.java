class Pgn
{
    String move;Board br;
    Pgn nxt,prv;
    int turn;
    boolean valid;
    Pgn()
    {
        br=new Board();
    }
    int begin(String fen)
    {
        br=new Board();
        String k=new Notation().FentoBoard(fen,br);
        if(k.indexOf("Invalid")!=-1)
        {
            System.out.println("File Corrupt");
            System.exit(0);
        }
        return k.charAt(0)-48;
    }
    Pgn(Board b,String mv,int c,int pro)
    {
        turn=c;
        move=mv;
        br=new Board();
        b.copy(br);
        valid=br.move(mv.substring(0,2),mv.substring(2,4),c,pro);
        prv=null;
        nxt=null;
    }
    boolean add(String mv,int c,int pro)
    {
       
        Pgn n=new Pgn(this.br,mv,c,pro);
        if(n.valid)
        {
          nxt=n;
          n.prv=this;
          return true;
        }
        else
         return false;
    }
}