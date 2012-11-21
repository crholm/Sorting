
public class InsertionSort implements SortingAlgorithm {

	@Override
	public int[] sort(int[] subject, int lowerLimit, int upperLimit) {
		int len = subject.length;
		
		 for (int i = 1; i < len; i++){
			  int j = i;
			  int B = subject[i];
			  while ((j > 0) && (subject[j-1] > B)){
				  subject[j] = subject[j-1];
				  j--;
			  }
			  subject[j] = B;
		  }
		  
		
		return subject;
	}

	@Override
	public String getName() {
		return "Insertion Sort";
	}

}
