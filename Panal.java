import java.awt.Rectangle;
import java.util.ArrayList;

public class Panal extends Rectangle{
	ArrayList<PanalElement> elems;
	
	public Panal() {
		super(Play.getSpaceWidth(), 0, Play.getPanalWidth(), Play.getHeight());
		elems = new ArrayList<PanalElement>();
		elems.add( new PanalElement(Play.getPanalWidth() + 30, 50, 50, 20, "reset") );
	}
}
