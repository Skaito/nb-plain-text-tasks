
package lt.realmdev.plaintexttasks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
		category = "Edit",
		id = "lt.realmdev.plaintexttasks.CancelTaskAction"
)
@ActionRegistration(
		displayName = "#CTL_CancelTaskAction"
)
@ActionReferences({
	@ActionReference(path = "Editors/text/plain/Popup", position = 402),
	@ActionReference(path = "Shortcuts", name = "DO-A")
})
@Messages("CTL_CancelTaskAction=Cancel Task")
public final class CancelTaskAction implements ActionListener {

	private final DataObject context;

	public CancelTaskAction(DataObject context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Task task = Task.findUnderCursor();
		task.setValue((task.getValue() != Task.Value.CANCEL) ? Task.Value.CANCEL : Task.Value.NEW);
	}
}
