
package lt.realmdev.plaintexttasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.text.NbDocument;

/**
 *
 */
public class Task {
	
	private StyledDocument doc;
	private JTextComponent editor;
	private int number, start, length;
	private Matcher m;
	/** If true then matcher m matched something else not. */
	private boolean mOk = false;
	private Value value = Value.NONE;
	private Date time = null;
	private SimpleDateFormat timeFormat;
	
	public enum Value { NONE, NEW, DONE, CANCEL }
	
	public Value getValue() {
		return value;
	}
	
	public void setValue(Value value) {
		if (value == null) value = Value.NONE;
		if (this.value != value) {
			time = Calendar.getInstance().getTime();
		}
		Value oldValue = this.value;
		int caretPos = editor.getCaretPosition();
		this.value = value;
		
		String cbVal, timeVal;
		switch (value) {
			case NEW:
				//cbVal = "[ ] ";
				cbVal = "\u2610 ";
				timeVal = "Created";
				break;
			case DONE:
				//cbVal = "[+] ";
				cbVal = "\u2714 ";
				timeVal = "Done";
				break;
			case CANCEL:
				//cbVal = "[x] ";
				cbVal = "\u2718 ";
				timeVal = "Canceled";
				break;
			default:
				cbVal = "";
				timeVal = "";
				break;
		}
		if (value != Value.NONE) timeVal = " @" + timeVal + "(" + timeFormat.format(time) + ")";
		int lenDiff = 0;
		int cbLen = (mOk ? m.group("cb").length() : 0), cbOff = (mOk ? m.start("cb") : 0);
		int timeLen = (mOk ? m.group("time").length() : 0), timeOff = (mOk ? m.start("time") : 0);
		try {
			if (cbLen > 0) doc.remove(start + cbOff, cbLen);
			if (value != Value.NONE) {
				NbDocument.insertGuarded(doc, start + cbOff, cbVal);
			}
			lenDiff -= (cbLen - cbVal.length());
			if (timeLen > 0) doc.remove(start + timeOff + lenDiff, timeLen);
			if (value != Value.NONE) NbDocument.insertGuarded(doc, start + timeOff + lenDiff, timeVal);
			boolean fromEmpty = (!mOk || m.group("content").trim().isEmpty());
			if (fromEmpty || oldValue == Value.NONE) {
				if (fromEmpty) {
					editor.setCaretPosition(start + cbOff + cbVal.length());
				} else {
					if (caretPos > (start + cbOff)) editor.setCaretPosition(caretPos + cbVal.length());
				}
			}
		} catch (BadLocationException ex) {}
		
	}
	
	public static Task findUnderCursor() {
		Task task = new Task();
		
		task.editor = EditorRegistry.lastFocusedComponent();
		task.timeFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		task.doc = (StyledDocument) task.editor.getDocument();
		task.number = NbDocument.findLineNumber(task.doc, task.editor.getCaretPosition());
		task.start = NbDocument.findLineOffset(task.doc, task.number);
		int lastLine = NbDocument.findLineNumber(task.doc, task.doc.getLength());
		if (task.number < lastLine) {			
			task.length = NbDocument.findLineOffset(task.doc, task.number + 1) - task.start;
		} else {
			task.length = task.doc.getLength() - task.start;
		}
		
		try {
			String line = task.doc.getText(task.start, task.length);
			Pattern p = Pattern.compile(
				"[\\ \\t]*" +
				"(?<cb>(?:\\[(?<cbValue>[^\\[\\]])\\]|(?<cbValueAlt>[\\u2610\\u2714\\u2718]))(?: |)|)" +
				"(?<content>[^\\n\\r\\@]+|)" +
				"(?<time> \\@[a-z]+\\((?<timeValue>[^\\(\\)]+)\\)|)(?:[\\r\\n]+|$)",
				Pattern.CASE_INSENSITIVE
			);
			task.m = p.matcher(line);
			task.mOk = false;
			if (task.m.matches()) {
				task.mOk = true;
				String cbVal = task.m.group("cbValue"), timeVal = task.m.group("timeValue");
				if (cbVal == null) cbVal = task.m.group("cbValueAlt");
				if (cbVal == null) cbVal = "";
				switch (cbVal) {
					case " ":
					case "\u2610":
						task.value = Value.NEW;
						break;
					case "+":
					case "*":
					case "\u2714":
						task.value = Value.DONE;
						break;
					case "-":
					case "x":
					case "\u2718":
						task.value = Value.CANCEL;
						break;
					default:
						task.value = Value.NONE;
						break;
				}
				task.time = (timeVal != null) ? task.timeFormat.parse(timeVal) : null;
			}
		} catch (BadLocationException | ParseException ex) {}
		if (task.time == null) task.time = Calendar.getInstance().getTime();
		
		return task;
	}
	
}
