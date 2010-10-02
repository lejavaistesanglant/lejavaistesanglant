import groovy.swing.SwingBuilder
import java.awt.*
import javax.swing.*
import java.util.*




class Signal extends Observable {

    def state=[:]


  void setState(String c,boolean checked){
    state[c]=checked
    setChanged(); // Positionne son indicateur de changement
    notifyObservers(this); // (1) notification 
  }

  public boolean getState(String key){
      return state[key]
  }

}

class ObsTextField extends JTextField implements Observer {
    
    private Signal signal
    public static int nb=0;
    String name
    public int id=0;

    public ObsTextField(name){
        this.name=name
        id=++nb;
    }


    public setSignal(Signal s){
        this.signal?.removeObserver(this)
        this.signal=s
        signal.addObserver(this)
    }

    public void update(Observable o, Object chk ) {

        if (chk == null ||   ((Signal)chk).getState(this.name)==false ){
            //System.out.println("Observer " +id+" "+this.name + " receive Disable order")
            this.editable=false;
            this.text="";
        }else{
            //System.out.println("Observer " +id+" "+this.name+ " receive Enable order")
            this.editable=true;
        }
    }
}

swing = new SwingBuilder()
signal = new Signal()

frame = swing.frame(title: "Groovy Chk", layout: new GridLayout(4, 3), size: [300, 150],
    defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {
        
        for (c in ["Infos Utiles", "Actus", "Toxin"]) {
            
            label(c)
            checkBox( name : c ,  action: swing.action(closure: { event ->
                            signal.setState(event.source.name, event.source.isSelected());
                      }));
            widget (new ObsTextField(c) ,editable : false, signal : signal )

        }
    }
frame.show()
