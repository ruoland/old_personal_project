package cmplus.cm.v18.function;

import java.io.File;
import java.util.Comparator;

public class FunctionComparator implements Comparator<File> {

	@Override
	public int compare(File file, File file2) {
		String o1 = ""+file.lastModified();
		String o2 = ""+file2.lastModified();

		return o2.compareTo(o1);
	}

}
