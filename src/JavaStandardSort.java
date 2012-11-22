import java.util.Arrays;


public class JavaStandardSort implements SortingAlgorithm {

	@Override
	public int[] sort(int[] subject, int lowerLimit, int upperLimit) {
		Arrays.sort(subject);
		return subject;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "JavaStandardSort";
	}

}
