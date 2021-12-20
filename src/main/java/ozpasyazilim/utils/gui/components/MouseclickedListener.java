package ozpasyazilim.utils.gui.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// note the absence of mouseClicked…
interface MouseclickedListener extends MouseListener {

	@Override
	public default void mouseEntered(MouseEvent e) {
	}

	@Override
	public default void mouseExited(MouseEvent e) {
	}

	@Override
	public default void mousePressed(MouseEvent e) {
	}

	@Override
	public default void mouseReleased(MouseEvent e) {
	}
}