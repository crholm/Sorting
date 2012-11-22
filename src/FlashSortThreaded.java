import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class FlashSortThreaded implements SortingAlgorithm{

	private int numberOfThreads = 6;
	private int threadsRunning = 0; 
	private int mergingThreadsRunning = 2; 
	private int[] subject;
	private int result[];
	private int len;
	private double a_min;
	private double a_range;
	private int scale = 2;
	private boolean isMerged = false;
	private ThreadPoolExecutor tp = new ThreadPoolExecutor(numberOfThreads+2, numberOfThreads+2, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	@Override
	public String getName() {
		return "FlashSortThreaded";
	}
	
	@Override
	public int[] sort(int[] subject, int lowerLimit, int upperLimit) {
		this.subject = subject;
		result = new int[scale*subject.length];
		
		
		
		len = subject.length;
		
		
		a_min = lowerLimit;
		a_range = upperLimit - lowerLimit;
		
		int increment = (int)((double)len/(double)numberOfThreads)+1;
		
		for(int i = 0; i < numberOfThreads; i++){
			int start = i*increment;
			int end = start + increment - 1;
			if(end >= len)
				end = len-1;
			
			tp.execute(new Worker(start, end, this));
			threadsRunning++;
		}
		
		while(threadsRunning != 0){
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {	}
		}		
		
//		long time = System.currentTimeMillis();
//		tp.execute(new MergingThread(false, subject, result));
//		tp.execute(new MergingThread(true, subject, result));
//		while(mergingThreadsRunning != 0){
//			try {
//				synchronized (tp) {
//					tp.wait();
//				}
//			} catch (InterruptedException e) {	}
//		}	
		
		merging(subject, result);
//		System.out.println("Mergin in Flashsort took:" + (System.currentTimeMillis() - time));
		
				
		isMerged = true;		
		synchronized (this) {
			this.notifyAll();
			threadsRunning = numberOfThreads-1;
		}
		
		while(threadsRunning != 0){
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {	}
		}	
		
//		long time = System.currentTimeMillis();
//		subject = insertionSort(subject, result);
//		System.out.println("Insertion in Flashsort took:" + (System.currentTimeMillis() - time));
		
		
		return subject;//insertionSort(subject);
	}
	
	
	public void merging(int[] target, int[] values){
//		long time = System.currentTimeMillis();
		int pointer = 0;
		for(int i = 0; i< values.length; i++){
			if(values[i] != 0){
				target[pointer] = values[i];
				pointer++;
			}
		}
//		System.out.println("Merging in Flashsort took:" + (System.currentTimeMillis() - time));

	}
	
	
	public int[] insertionSort(int[] target) {
		
//		long time = System.currentTimeMillis();
		int len = target.length;
		for (int i = 1; i < len; i++){
			  int j = i;
			  int B = target[i];
			  while ((j > 0) && (target[j-1] > B)){
				  target[j] = target[j-1];
				  j--;
			  }
			  target[j] = B;
		  }
//		System.out.println("Final Insertion in Flashsort took:" + (System.currentTimeMillis() - time));
		
		return target;
	}
	
	



	
	private class MergingThread implements Runnable{
		private boolean inReverse;
		int target[], values[];
		FlashSortThreaded parent;
		
		public MergingThread(boolean inReverse, int target[], int values[]){
			this.inReverse = inReverse;
			this.target = target;
			this.values = values;
		}
		
		@Override
		public void run() {
			//System.out.println("Merging thread Started");
			if(!inReverse){
				//System.out.println("First Half: " + 0 +"-" + ((values.length/2)));
				int pointer = 0;
				for(int i = 0; i < values.length/2; i++){
					if(values[i] != 0){
						target[pointer] = values[i];
						pointer++;
					}
				}
			}	
			else{
				//System.out.println("Secound Half: " + (values.length/2+1) +"-" + (values.length-1));
				int pointer = target.length-1;
				for(int i = values.length-1; values.length/2 < i; i--){
					if(values[i] != 0){
						target[pointer] = values[i];
						pointer--;
					}
				}
			}
				
			synchronized (tp) {
				mergingThreadsRunning--;
				tp.notifyAll();
			}
			
		}
		
	}
	
	private class Worker implements Runnable{
		int start;
		int end;
		FlashSortThreaded parent;
		public Worker(int start, int end, FlashSortThreaded parent){
			this.start = start;
			this.end = end;
			this.parent = parent;
		}
		
		
		@Override
		public void run() {
			int index;
			for(int i = start; i < end; i++){
				index = 1+(int)(  scale*((len-1)*((double)subject[i]-a_min)) / (a_range) );
				add(subject[i], result, index, len);
			}
	
			synchronized (parent) {
				threadsRunning--;
				parent.notifyAll();
			}
			
			while(!isMerged){
				try {
					synchronized (parent) {
						parent.wait();
					}
				} catch (InterruptedException e) {	}
			}
			insertionSort(subject, start, end);
			
			synchronized (parent) {
				threadsRunning--;
				parent.notifyAll();
			}
			
		}
	
		
		public int[] insertionSort(int[] target, int start, int stop) {
//			long time = System.currentTimeMillis();
			int len;
//			if(start == 0){
				len = target.length;
//			}
//			else
//				len = stop+1;

			//target.length;
			for (int i = start+1; i < len; i++){
				  int j = i;
				  int B = target[i];
				  while ((j > 0) && (target[j-1] > B)){
					  target[j] = target[j-1];
					  j--;
				  }
				  target[j] = B;
			  }
//			System.out.println("Insertion in Flashsort took:" + (System.currentTimeMillis() - time));
			
			return target;
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
