import groovy.swing.SwingBuilder
import java.awt.*
import javax.swing.*
import java.util.*



class Model extends Observable {
    static CURRENCY = ["USD", "EURO", "YEN"]

    private Map rates = new HashMap()
    private long value

    void initialize(initialRates) {
        (0..CURRENCY.size() - 1).each {
            setRate(CURRENCY[it], initialRates[it])
        }
    }

    void setRate(currency, f) {
	    rates.put(currency, f);
	    setChanged();
	    notifyObservers(currency);
    }

    void setValue(currency, double newValue) {
	    value = Math.round(newValue / rates[currency]);
	    setChanged();
	    notifyObservers(null);
    }

    def getValue(currency) {
        value * rates[currency]
    }
}

class RateView extends JTextField implements Observer {
    private Model model;
    private currency;

    public void setModel(Model model) {
        this.model?.removeObserver(this)
        this.model = model
        model.addObserver(this)
    }

    public void update(Observable o, Object currency) {
        if (this.currency == currency)
            text = String.format("%15.2f", model.rates[currency])
    }
}

class ValueView extends JTextField implements Observer {
    private Model model
    private currency

    public void setModel(Model model) {
        this.model?.removeObserver(this)
        this.model = model
        model.addObserver(this)
    }

    public void update(Observable o, Object currency) {
        if (currency == null || this.currency == currency)
            text = String.format("%15.2f", model.getValue(this.currency));
    }
}


swing = new SwingBuilder()
model = new Model()

frame = swing.frame(title: "Groovy SwingBuilder MVC Demo", layout: new GridLayout(4, 3), size: [300, 150],
    defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {

        label("currency")
        label("rate")
        label("value")

        for (c in Model.CURRENCY) {
            label(c)
            widget(new RateView(), model: model, currency: c,
                     action: swing.action(closure: { event ->
                            event.source.model.setRate(event.source.currency, event.source.text.toDouble());
                      }))
            widget(new ValueView(), model: model, currency: c, action: swing.action(closure: {event ->
                            event.source.model.setValue(event.source.currency, event.source.text.toDouble());
                      }))
        }
    }

frame.show()
model.initialize([1.0, 0.83, 0.56]);