import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class Node{
	int data;
	Node left;
	Node right;
	
	public Node(int item) {
		data = item;
		left = null;
		right = null;
	}
}
public class decoder {
static Node root = new Node(-1);
	
	public  void decodeOutputStream(String code_table_file_name,String encoded_file_name){
			Map<Integer, String> codeTable = getCodeTable(code_table_file_name);
			constructDecodeTree(codeTable);
			String encodedString = getEncodedInputString(encoded_file_name);
			decodeString(encodedString,root);
	}
	
	private static void decodeString(String encodedString, Node root) {
		// TODO Auto-generated method stub
// 		List<Integer> decodedData = new ArrayList<Integer>();
		int counter=0;
 		FileWriter fw=null;
		BufferedWriter bw=null;
		Node node = root;
		try {
			fw = new FileWriter("decoded.txt");
			bw = new BufferedWriter(fw);
			 char[] c = encodedString.toCharArray();
			 for (int bitcounter = 0; bitcounter < encodedString.length(); bitcounter++) {
				 if (node!=null && c[bitcounter] == '0') {
					 
		            	node = node.left;
		            	if(node.left == null && node.right == null){
		            		if(counter!=0)
							 {
								 bw.write('\n'); 
							 }
		            		bw.write(""+node.data);
		            		node = root;
		            		//if( bitcounter != encodedString.length() - 1){
		            			counter++;
		            		}
					 	}
		           // } 
		            else if (node!=null && c[bitcounter] == '1') {
		            	
		            	node = node.right;
		            	if(node.left == null && node.right == null){
		            		if(counter!=0)
							 {
								 bw.write('\n'); 
							 }
		            		bw.write(""+node.data);
		            		node = root;
		            		//if( bitcounter != encodedString.length() - 1){
		            			//bw.write('\n');
		            		//}
		            		counter++;
					 	}
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
	}

	public static Map<Integer, String> getCodeTable(String code_table_file_name) {
		// TODO Auto-generated method stub
		BufferedReader br;
		String line;
		Map<Integer, String> codeTable = new HashMap<Integer, String>();
		try {
		br = new BufferedReader(new FileReader(code_table_file_name));
		while ((line = br.readLine()) != null && !line.isEmpty()) {
	    	String[] data = line.split(" ");
	    	codeTable.put(new Integer(data[0]), data[1]);
	    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
    	return codeTable;
	}

	private static String getEncodedInputString(String encoded_file_name) {
		// TODO Auto-generated method stub
		StringBuilder encodedInput = new StringBuilder();
		File file = new File(encoded_file_name);
		byte[] data = new byte[(int)file.length()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BitSet bitset = new BitSet();
		bitset = BitSet.valueOf(data);
		for(int i = 0; i < data.length * 8; i++){
			if(bitset.get(i)){
				encodedInput.append('1');
			}
			else{
				encodedInput.append('0');
			}
		}
		return encodedInput.toString();
	}

	private static void constructDecodeTree(Map<Integer, String> codeTable) {
		// TODO Auto-generated method stub
		for (Map.Entry<Integer, String> entry : codeTable.entrySet()) {
            createElementNode(entry.getKey(),entry.getValue());
        }
	}

	private static void createElementNode(Integer key,String value) {
		// TODO Auto-generated method stub
		Node node = root;
		Node element = new Node(key);
		char[] bitarray = value.toCharArray();
		for(int i=0; i < bitarray.length - 1 ;i++){
			if(bitarray[i] == '0'){
				if(node.left == null){
					node.left = new Node(-1);
				}
				node = node.left;
			}
			else if(bitarray[i] == '1'){
				if(node.right == null){
					node.right = new Node(-1);
				}
				node = node.right;
			}
		}
		if(bitarray[bitarray.length - 1] == '0'){
			if(node.left == null){
				node.left = element;
			}
		}
		else{
			if(node.right == null){
				node.right = element;
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		decoder hd=new decoder();
		if(args.length==0 || args.length==1){
			System.out.println("Invalid Invocation for the huffman encoder");
			System.exit(0);
		}else{
			String encoded_file_name=args[0];
			String code_table_file_name=args[1];
			//String encodedString=hd.getEncodedInputString();
			hd.decodeOutputStream(code_table_file_name,encoded_file_name);
			
		}
	//	HuffmanDecoder.decodeOutputStream();
		
	}
}
