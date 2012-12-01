import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class FlashSortSampled implements SortingAlgorithm {


	
	
	private int numberOfThreads = 6;
	private ThreadPoolExecutor tp = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private int runningThreads = 0;
	
	
	@Override
	public int[] sort(int[] subject, int lowerLimit, int upperLimit) {
		long time = System.currentTimeMillis();
		
		int bucket[][] = new int[numberOfThreads][(int)(subject.length*1.2/numberOfThreads)];
		int bucketLen[] = new int[numberOfThreads];
				
		System.out.println("Time Creating buckets: " + (System.currentTimeMillis()-time) + "ms");
		time = System.currentTimeMillis();
		int location = 0;
		for(int i = 0; i < subject.length; i++){
				location = (int)((double)(numberOfThreads)*((double)(subject[i]-lowerLimit)) / ((double)(upperLimit - lowerLimit)));
				bucket[location][bucketLen[location]] = subject[i];
				bucketLen[location]++;
		}
		
		System.out.println("Time Filling buckets: " + (System.currentTimeMillis()-time) + "ms");
		
		
		time = System.currentTimeMillis();
		LinkedList<FlashSortThread> threads = new LinkedList<FlashSortThread>();
		
		int low;
		int high;
		int inc = (upperLimit - lowerLimit)/numberOfThreads;
		
		for(int i = 0; i < numberOfThreads; i++){
			low = lowerLimit  + i*inc; 
			high = low + inc-1;
			if(i+1 == numberOfThreads)
				high = upperLimit;
			
			runningThreads++;
			FlashSortThread t = new FlashSortThread(bucket[i], bucketLen[i] , low, high);
			threads.add(t);
			tp.execute(t);				
		}
		
		System.out.println("Time Settring up threads: " + (System.currentTimeMillis()-time) + "ms");
		time = System.currentTimeMillis();
		
		
		while(runningThreads != 0){
			synchronized (tp) {
				try {
					tp.wait();
				} catch (InterruptedException e) {}
			}
		}
		
		System.out.println("Time For flash sort: " + (System.currentTimeMillis()-time) + "ms");
		time = System.currentTimeMillis();
		
		
		int offset = 0;
		for(int i = 0; i< numberOfThreads; i++){
			System.arraycopy(bucket[i], 0, subject, offset, bucketLen[i]);
			offset += bucketLen[i]; 
		}
		System.out.println("Time Mergin results: " + (System.currentTimeMillis()-time) + "ms");
		time = System.currentTimeMillis();
		
		
		return subject;
	}

	@Override
	public String getName() {
		return "FlashSortSampled";
	}	
	
	private class FlashSortThread implements Runnable{
		private int subjectLen;
		private int[] subject;
		private int[] expandedSubject;
		private int lowerLimit;
		private int upperLimit;
		private int scale = 2;
		
		public FlashSortThread(int[] subject, int subjectLen, int lowerLimit, int upperLimit){
			this.subject = subject;
			this.subjectLen = subjectLen;
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
		
		public int[] getSubject(){
			return subject;
		}
		
		@Override
		public void run() {
			expand();
			reduce();
			sort();		
			
			synchronized (tp) {
				runningThreads--;
				tp.notifyAll();
			}
		}
		
		
		public void expand(){
			expandedSubject = new int[scale*subject.length];
			
			int len = subjectLen;
			int index;
			
			double a_min = lowerLimit;
			double a_range = upperLimit - lowerLimit;
			
			
			for(int i = 0; i < len; i++){
				index = 1+(int)(  scale*((len-1)*((double)subject[i]-a_min)) / (a_range)  );
				
				add(subject[i], expandedSubject, index, len);
			}
		}
		
	
		public void reduce(){
			int pointer = 0;
			for(int i = 0; i< expandedSubject.length; i++){
				if(expandedSubject[i] != 0){
					subject[pointer] = expandedSubject[i];
					pointer++;
				}
			}
		}
		
		
		public void  sort() {
			int len = subjectLen;
			for (int i = 1; i < len; i++){
				  int j = i;
				  int B = subject[i];
				  while ((j > 0) && (subject[j-1] > B)){
					  subject[j] = subject[j-1];
					  j--;
				  }
				  subject[j] = B;
			  }
		}
		
	
		
		private void add(int toBeAdded, int[] result, int index, int len){
			if(result[index] == 0){
				result[index] = toBeAdded;
				return;
			}else if(result[index] <= toBeAdded){
				int i = 1;
				while(index+i < len){
					if(result[index+i] == 0){
						result[index+i] = toBeAdded;
						return;
					}
					i++;
				}
				i=1;
				while(index-i >= 0){
					if(result[index-i] == 0){
						result[index-i] = toBeAdded;
						return;
					}
					i++;
				}		
			}else{
				int i = 1;
				while(index-i >= 0){
					if(result[index-i] == 0){
						result[index-i] = toBeAdded;
						return;
					}
					i++;
				}
				i=1;
				while(index+i < len){
					if(result[index+i] == 0){
						result[index+i] = toBeAdded;
						return;
					}
					i++;
				}
			}
		}
		
		
		
		
	}

}
