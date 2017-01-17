
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
		id = "lt.realmdev.plaintexttasks.DoneTaskAction"
)
@ActionRegistration(
		displayName = "#CTL_DoneTaskAction",
		key = "DO-D"
)
@ActionReferences({
	@ActionReference(path = "Editors/text/plain/Popup", position = 401),
	@ActionReference(path = "Shortcuts", name = "DO-D")
})
@Messages("CTL_DoneTaskAction=Done Task")
public final class DoneTaskAction implements ActionListener {

	private final DataObject context;

	public DoneTaskAction(DataObject context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Task task = Task.findUnderCursor();
		task.setValue((task.getValue() != Task.Value.DONE) ? Task.Value.DONE : Task.Value.NEW);
	}
}
