import groovy.swing.SwingBuilder
import java.awt.*
import javax.swing.*
import java.util.*




class Signal extends Observable {

  boolean state =false;

  void setState(boolean checked){
    state=checked
    System.out.println("state is "+state)
    setChanged(); // Positionne son indicateur de changement
    notifyObservers(); // (1) notification
  }
}



class ObsTextField extends JTextField implements Observer {
    
    Signal signal

    /*public void setModel(Model model) {
        this.model?.removeObserver(this)
        this.model = model
        model.addObserver(this)
    }*/

    public setSignal(Signal s){
        this.signal?.removeObserver(this)
        this.signal=s
        signal.addObserver(this)
        System.out.println("Observer receive Signal "+s)
    }

    public void update(Observable o, Object chk ) {
        if (chk == null ||  ! chk.getState() ){
            System.out.println("Observer receive Disable order")
            this.disable()
        }else{
            System.out.println("Observer receive Enable order")
            this.enable()
        }
    }
}


swing = new SwingBuilder()
signal = new Signal();

frame = swing.frame(title: "Groovy Chk", layout: new GridLayout(4, 3), size: [300, 150],
    defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {

        
        for (c in ["Infos Utiles","Actus", "Toxin"]) {
            label(c)
            checkBox( text:c, action: swing.action(closure: { event ->
                            signal.setState(event.source.isSelected());
                      }));
            widget(new ObsTextField(), signal: signal )

/*

            widget(new ValueView(), model: model, currency: c, action: swing.action(closure: {event ->
                            event.source.model.setValue(event.source.currency, event.source.text.toDouble());
                      }))
*/
        }
    }
frame.show()
/*model.initialize([1.0, 0.83, 0.56]);*/