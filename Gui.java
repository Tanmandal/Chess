import javax.swing.*;
import java.awt.*;
class Gui 
{
    JFrame frame;
    JPanel squares[][] = new JPanel[8][8];
    void gui(Board b,boolean rot) 
    {
        frame = new JFrame("Simplified Chess");
        frame.setSize(500, 500);
        frame.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
           for (int j = 0; j < 8; j++) {
               squares[i][j] = new JPanel();
               switch(b.sq[rot?i:7-i][rot?j:7-j].cl)
               {
                  case 0:squares[i][j].setBackground(new Color(127, 127, 127));break;
                  case 1:squares[i][j].setBackground(Color.white);break;
                  case 2:squares[i][j].setBackground(new Color(222,148,222));break;
                  case 3:squares[i][j].setBackground(new Color(227,167,109));break;
               }

                /*if ((i + j) % 2 == 0) {
                   squares[i][j].setBackground(new Color(127, 127, 127));
                } else {
                    squares[i][j].setBackground(Color.white);
                } */  
                frame.add(squares[i][j]);
            }
        }
        String p="",cr="";
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(!b.sq[i][j].isEmpty())
                {
                    switch(b.sq[i][j].pce.pc)
                    {
                        case 1:p="king";break;
                        case 2:p="queen";break;
                        case 3:p="bishop";break;
                        case 4:p="knight";break;
                        case 5:p="rook";break;
                        case 6:p="pawn";break;
                    }
                    switch(b.sq[i][j].pce.col)
                    {
                        case 1:cr="white";break;
                        case 2:cr="black";break;
                    }
                    squares[rot?i:7-i][rot?j:7-j].add(new JLabel(new ImageIcon("Pic/"+p+cr+".png")));
                }
            }
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    void dis()
    {
        frame.dispose();
    }
}
