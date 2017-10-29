import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class encoder {
	
	HashMap<Integer, String> hmap = new HashMap<Integer, String>();
	
	public class HuffmanNodes{
		int data;
		int freq;
		HuffmanNodes left;
		HuffmanNodes right;
		
		HuffmanNodes(int data,int freq){
			this.data=data;
			this.freq=freq;
			this.left=null;
			this.right=null;
		}
	}

	public class FourWayHeapFreq {
		

		int capacity; // maximum possible size 
	    int heapSize;
	    HeapNodes heapArr[];
	    
	    FourWayHeapFreq(int capacity){
	    	this.capacity=capacity;
	    	this.heapSize=3;
	    	heapArr=new HeapNodes[capacity];
	    	heapArr[0]=new HeapNodes(new HuffmanNodes(0,-1));//make the node -1
	    	heapArr[1]=new HeapNodes(new HuffmanNodes(0,-1));//make the node -1 
	    	heapArr[2]=new HeapNodes(new HuffmanNodes(0,-1));//make the node -1
	    }
	    
	    
	    
	    public class HeapNodes{
	    	int data;
	    	int freq;
	    	HuffmanNodes hnodes;
	    	HeapNodes(HuffmanNodes hnodestemp){
	    		this.data=hnodestemp.data;
	    		this.freq=hnodestemp.freq;
	    		this.hnodes=hnodestemp;

	    	}
	    }
	    
	    public int parent(int i) 
	    {
	    	
	        return (int)Math.round((i-2)/4.0+2);
	    }
	 
	    /** Function to get index of k th child of i **/
	    public int kthChild(int i, int k) 
	    {
	    	
	        return (4 * (i-2) + (k-1));
	    }
	    
	    public void insert(HuffmanNodes x)
	    {
	    	HeapNodes hn=new HeapNodes(x);
	    	heapArr[heapSize++] = hn;
	        heapifyUp(heapSize - 1);
	    }
	    
	    public HuffmanNodes ExtractMin(){
	    	HeapNodes minimum=heapArr[3];
	    	HeapNodes root = this.heapArr[3];
	        this.heapArr[3] = this.heapArr[this.heapSize-1];
	        this.heapSize--;
	        heapifyDown(3);	
	    	return minimum.hnodes;
	    }
	    
	    public void heapifyDown(int index){
	    	int child;
	    	HeapNodes tmp = heapArr[ index ];
	        while (kthChild(index, 1) < heapSize)
	        {
	            child = minChild(index);
	            if (heapArr[child].freq < tmp.freq)
	            	heapArr[index] = heapArr[child];
	            else
	                break;
	            index = child;
	        }
	        heapArr[index] = tmp;
	    }
	    
	    public int minChild(int index){
	    	int Child = kthChild(index, 1);
	        int k = 2;
	        int pos = kthChild(index, k);
	        while ((k <= 4) && (pos < heapSize)) 
	        {
	            if (heapArr[pos].freq < heapArr[Child].freq) 
	                Child = pos;
	                k++;
	            pos = kthChild(index, k);
	        }    
	        return Child;
	    }
	    
	    public void heapifyUp(int index)
	    {
	    	HeapNodes element = heapArr[index];  
	    	HeapNodes temp;
	        while (index > 0 && element.freq < heapArr[parent(index)].freq)
	        {
	        	temp=heapArr[index];
	        	heapArr[index] = heapArr[ parent(index) ];
	        	heapArr[parent(index)]=temp;
	            index = parent(index);
	        }
	     }
	    
	    public void printElements(){
	    	//System.out.println("Entered the Prin Element");
	    	for(int i=0;i<heapSize;i++){
	    		System.out.println(heapArr[i]);
	    	}
	    	//System.out.println("Finished the Prin Element");
	    }
	    
	    void HuffmanCoding(int arr[]){
	    	FourWayHeapFreq bhwf=new FourWayHeapFreq(arr.length); 
	    	for(int i=0;i<arr.length;i++){
	    		if(arr[i]!=0){
	    			HuffmanNodes hnodes2=new HuffmanNodes(i,arr[i]);
		    		bhwf.insert(hnodes2);
	    		}	
	    	}
	    	
	    	while(bhwf.heapSize!=4){
	    		HuffmanNodes left=bhwf.ExtractMin();
	    		HuffmanNodes right=bhwf.ExtractMin();
	    		//HuffmanNodes hnode2=new HuffmanNodes()
	    		HuffmanNodes top= new HuffmanNodes(Integer.MIN_VALUE,left.freq+right.freq);
	    		top.left=left;
	    		top.right=right;
	    		bhwf.insert(top);
	    	}
	    	
	    	printCodes(bhwf.heapArr[3].hnodes,"");
	    	
	    }
	    
	    
	    void printCodes(HuffmanNodes root,String str){
	    	 if (root==null)
	    	        return;
	    	 
	    	 if(root.data!=Integer.MIN_VALUE){
	    		 hmap.put(root.data, str);
	    	 }
	    	 
	    	 printCodes(root.left,str+"0");
	    	 printCodes(root.right,str+"1"); 
	    }
	    
	}
	
	
	  
	    
	    void buildHuffmanTrees(int arr[]){
	    	FourWayHeapFreq bhwf=new FourWayHeapFreq(arr.length);
				
	    		bhwf.HuffmanCoding(arr);	
	    		
	    }
	    
	    
	    String encodedOutput(ArrayList fileContentarr){
			
			StringBuilder encoded = new StringBuilder();
			System.out.println(fileContentarr.size());
			for(int i=0;i<fileContentarr.size();i++){
				encoded.append((String)hmap.get(fileContentarr.get(i)));
			}
			return encoded.toString();
		}
		
		void createEncodedFile(String encoded){
			//System.out.println("Enterd the file creation stage");
			BitSet bitSet = new BitSet(encoded.length());
			int bitcounter = 0;
			for(Character c : encoded.toCharArray()) {
			    if(c.equals('1')) {
			        bitSet.set(bitcounter);
			    }
			    bitcounter++;
			}
			
			byte encodedArr[]=bitSet.toByteArray();

			try {
				FileOutputStream fos = new FileOutputStream("encoded.bin");
				BufferedOutputStream bos = null;
				bos = new BufferedOutputStream(fos);
				try {
					bos.write(encodedArr);
					bos.flush();
					//System.out.println("Done with flushing");
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					 try {
						 if(bos!=null){
							 bos.flush();
							 bos.close(); 
						 }
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
		
		public void createCodeTableFile(String FILENAME){
			FileWriter fw=null;
			BufferedWriter bw=null;
			try {
				fw = new FileWriter(FILENAME);
				bw = new BufferedWriter(fw);
				 Iterator it = hmap.entrySet().iterator();
			        while (it.hasNext()) {
			        	Map.Entry hmapValue = (Map.Entry)it.next();
			        	//System.out.println(Integer.toString((int)hmapValue.getKey())+" "+(String)hmapValue.getValue());
			        	try {
							bw.write(Integer.toString((int)hmapValue.getKey()) + " " + (String)hmapValue.getValue());
							if(it.hasNext()){
								bw.write("\n");
							}
							//bw.write(hmapValue.getKey()+" "+hmapValue.getValue()+"\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						  }	
						}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				if(bw!=null){
					try {
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fw!=null){
				  try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			}      
	        System.out.println("Created Code Table : code_table.txt");
		}
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		
		encoder hcbh=new encoder();
		
		int arr[]=new int[100000000];
		ArrayList <Integer> fileContentarr=new ArrayList<Integer>();
		int i=0;
		Arrays.fill(arr, 0);	
		if(args.length==0){
			System.out.println("Please Enter the file Path");
			System.exit(0);
		}else{
			String FILENAME=args[0];
			String currentLine;
			
			try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
				while ((currentLine = br.readLine()) != null && !currentLine.equals("")) {
					int value=Integer.parseInt(currentLine);
					arr[value]=arr[value]+1;
					fileContentarr.add(value);
				}
				long startTime = System.nanoTime();
				for(i=0;i<10;i++){
					//HuffmanCodingFourwayHeap hcbh=new HuffmanCodingFourwayHeap();
				hcbh.buildHuffmanTrees(arr);
			}
				long stopTime = System.nanoTime();
				System.out.println("Time for 10 iterations in seconds "+ (stopTime - startTime)/ 1000000000.0);
				hcbh.buildHuffmanTrees(arr);
				String encodedInput=hcbh.encodedOutput(fileContentarr);
				hcbh.createEncodedFile(encodedInput);
				hcbh.createCodeTableFile("code_table.txt");
				
			}catch (IOException e) {
					e.printStackTrace();
		    }
        }
	}

}
