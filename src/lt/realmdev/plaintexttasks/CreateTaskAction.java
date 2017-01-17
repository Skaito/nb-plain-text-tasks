
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
		id = "lt.realmdev.plaintexttasks.CreateTaskAction"
)
@ActionRegistration(
		displayName = "#CTL_CreateTaskAction",
		key = "DO-C"
)
@ActionReferences({
	@ActionReference(path = "Editors/text/plain/Popup", position = 400, separatorAfter = 450),
	@ActionReference(path = "Shortcuts", name = "DO-C")
})
@Messages("CTL_CreateTaskAction=Create Task")
public final class CreateTaskAction implements ActionListener {

	private final DataObject context;

	public CreateTaskAction(DataObject context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Task task = Task.findUnderCursor();
		task.setValue((task.getValue() == Task.Value.NONE) ? Task.Value.NEW : Task.Value.NONE);
	}
	
}
