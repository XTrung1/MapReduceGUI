package frames;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import frames.Main.MST_Mapper;
import frames.Main.MST_Reducer;
import frames.Main.MST_Reducer.Edge;
import frames.Main.MST_Reducer.Kruskal;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField inputFile;
	private static HadoopInteractor hadoopInteractor;
	public String FOLDER_NAME_INPUT = "input";
	public String FILE_NAME_INPUT = "input.txt";
	public String FOLDER_NAME_OUTPUT = "output";
	public String CLASS_NAME = "Main";
	public String APP_ICON_LINK = "/images/hadoop.png";
	public static Path[] input = new Path[10];
	public ArrayList<String> Multifile = new ArrayList<>();
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Main() throws IOException {
		try {
		    hadoopInteractor = new HadoopInteractor();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 532);
		contentPane = new JPanel();
		contentPane.setBounds(new Rectangle(10, 10, 4, 4));
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		setTitle("MapReduce Application");
		setLocationRelativeTo(null);
		JScrollPane scrollInput = new JScrollPane();
		scrollInput.setBounds(new Rectangle(10, 10, 4, 4));
		scrollInput.setBorder(null);
		scrollInput.setBounds(10, 104, 330, 376);
		contentPane.add(scrollInput);
		Font titleFont = new Font("Times New Roman", Font.BOLD, 20);
		JTextArea textInput = new JTextArea();
		textInput.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		textInput.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 3, true), "Content", TitledBorder.CENTER, TitledBorder.TOP, titleFont, new Color(219, 0, 0)));
		textInput.setLineWrap(true);
		textInput.setWrapStyleWord(true);
		
		scrollInput.setViewportView(textInput);
		scrollInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    JScrollPane scrollOutput = new JScrollPane();
	    scrollOutput.setBorder(null);
	    scrollOutput.setBounds(350, 104, 326, 376);
	    contentPane.add(scrollOutput);
	    
	    JTextArea textOutput = new JTextArea();
	    textOutput.setSelectionColor(new Color(192, 192, 192));
	    textOutput.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	    textOutput.setEditable(false);
	    textOutput.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 3, true), "Result", TitledBorder.CENTER, TitledBorder.TOP, titleFont, new Color(0, 128, 0)));
	    scrollOutput.setViewportView(textOutput);
	    
	    JButton btnRun = new JButton("RUN");
	
	    btnRun.setForeground(new Color(0, 0, 0));
	    btnRun.setBackground(new Color(192, 192, 192));
	    btnRun.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
	    btnRun.setFont(new Font("Times New Roman", Font.BOLD, 18));
	    btnRun.setBounds(577, 10, 99, 30);
	    contentPane.add(btnRun);
	    
	    inputFile = new JTextField();
	    inputFile.setFont(new Font("Times New Roman", Font.PLAIN, 14));
	    inputFile.setBackground(new Color(255, 255, 255));
	    inputFile.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
	    inputFile.setBounds(10, 12, 240, 30);
	    contentPane.add(inputFile);
	    inputFile.setColumns(10);
	    inputFile.setEditable(false);
	    
	    JButton btnBrowser = new JButton("Browser");
	    btnBrowser.setForeground(Color.BLACK);
	    btnBrowser.setFont(new Font("Times New Roman", Font.BOLD, 18));
	    btnBrowser.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
	    btnBrowser.setBackground(new Color(192, 192, 192));
	    btnBrowser.setBounds(260, 12, 79, 30);
	    
	    JFrame frame = new JFrame("JFileChooser Example");
        frame.getContentPane().setLayout(null);
	    
	    btnBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
               
                int result = fileChooser.showOpenDialog(frame);
                
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    inputFile.setText(selectedFile.getAbsolutePath());
                }
            }
        });
	    contentPane.add(btnBrowser);
	    
	    JPanel panel = new JPanel();
	    panel.setBackground(new Color(255, 255, 255));
	    panel.setBounds(10, 54, 240, 33);
	    contentPane.add(panel);
	    panel.setLayout(null);
	    
	    JLabel lblNewLabel_1 = new JLabel("Input Type:");
	    lblNewLabel_1.setBounds(5, 4, 91, 24);
	    lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	    lblNewLabel_1.setBackground(new Color(255, 255, 255));
	    panel.add(lblNewLabel_1);
	    
	    JRadioButton rdbtn_Content = new JRadioButton("Content");
	    rdbtn_Content.setBackground(new Color(255, 255, 255));
	    rdbtn_Content.setBounds(102, 2, 83, 29);
	    rdbtn_Content.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	    rdbtn_Content.setSelected(true);
	    panel.add(rdbtn_Content);
	
	    
	    JRadioButton rdbtn_File = new JRadioButton("File");
	    rdbtn_File.setBackground(new Color(255, 255, 255));
	    rdbtn_File.setBounds(187, 2, 53, 29);
	    rdbtn_File.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	    panel.add(rdbtn_File);
	    
	    ButtonGroup btnGroup = new ButtonGroup();
	    btnGroup.add(rdbtn_File);
	    btnGroup.add(rdbtn_Content);
	    
	    btnRun.setEnabled(false);
	    
	    rdbtn_Content.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!textInput.getText().isEmpty()){
					if (!btnRun.isEnabled()){
						btnRun.setEnabled(true);
					}
				}else{
					if (btnRun.isEnabled()){
						btnRun.setEnabled(false);
					}
				}
				
			}
		});
	    
	    rdbtn_File.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!inputFile.getText().isEmpty()){
					if (!btnRun.isEnabled()){
						btnRun.setEnabled(true);
					}
				}else{
					if (btnRun.isEnabled()){
						btnRun.setEnabled(false);
					}
				}
			}
		});
	    
	    JButton btnQuit = new JButton("QUIT");
	    btnQuit.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
	    btnQuit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		System.exit(0);
	    	}
	    });
	    btnQuit.setBackground(new Color(192, 192, 192));
	    btnQuit.setFont(new Font("Times New Roman", Font.BOLD, 18));
	    btnQuit.setBounds(577, 54, 99, 34);
	    contentPane.add(btnQuit);
	    
	    JButton btnReset = new JButton("Reset");
	    btnReset.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
	    
	    
	    btnReset.setFont(new Font("Times New Roman", Font.BOLD, 18));
	    btnReset.setBackground(new Color(192, 192, 192));
	    btnReset.setBounds(260, 53, 80, 34);
	    contentPane.add(btnReset);
	    
	    JComboBox<String> options = new JComboBox<String>();
	    options.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
	    options.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	    options.setBounds(350, 58, 217, 30);
	    
	    options.addItem("Word Count");
	    options.addItem("Minimum Spanning Tree");
	    
	    contentPane.add(options);
	    
	    JLabel text_options = new JLabel("PROGRAM");
	    text_options.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	    text_options.setBounds(414, 25, 99, 27);
	    contentPane.add(text_options);
	    
		Image image = new ImageIcon(getClass().getResource(APP_ICON_LINK)).getImage();
		setIconImage(image);
		
		btnReset.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e){
		    		ClearFilesInHadoop();
		    		textInput.setText("");
		    		inputFile.setText("");
		    		rdbtn_Content.setEnabled(true);
		    		textOutput.setText("");	    		
		    	}
		    });
		    
		rdbtn_Content.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputFile.getText().isEmpty()){
					return;
				}else {
					textInput.setText("");
				}
			}
		});
		
		rdbtn_File.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputFile.getText().isEmpty()){
					if (btnRun.isEnabled()){
						btnRun.setEnabled(false);
					}else{
						String content;
						try {
							content = new String(Files.readAllBytes(Paths.get(inputFile.getText())));
							textInput.setText(content);
						} catch (IOException e1) {
							System.out.println("Không hiển thị nội dung file ra được ní ơi !");
						}
					}
				}
			}
		});
		
		textInput.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e){
				onChange();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e){
				onChange();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e){
				onChange();
			}
			
			public void onChange() {
				if (rdbtn_Content.isSelected()){
					if (!textInput.getText().isEmpty()){
						if (!btnRun.isEnabled()) {
							btnRun.setEnabled(true);
						}
					}else {
						if (btnRun.isEnabled()) {
							btnRun.setEnabled(false);
						}
					}
				}
			}
		});
		
		inputFile.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				onChange();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				onChange();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				onChange();
			}
			
			public void onChange(){
				rdbtn_File.setSelected(true);
				if (rdbtn_File.isSelected() && !inputFile.getText().isEmpty()){
					if (!btnRun.isEnabled()){
						btnRun.setEnabled(true);
						try {
							String content = new String(Files.readAllBytes(Paths.get(inputFile.getText())));
							textInput.setText(content);
						} catch (Exception e) {
							System.out.println("Không hiển thị nội dung trong file ra được !");
						}
						
					}
				}else {
					if (btnRun.isEnabled()) {
						btnRun.setEnabled(false);
					}
				}
			}
		});
		
	    btnRun.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e){
	    		Boolean condition1 = !inputFile.getText().isEmpty() && rdbtn_File.isSelected();
	    		Boolean condition2 = !textInput.getText().isEmpty() && rdbtn_Content.isSelected();
	    		
	    		if (condition1 && condition2){
	    			JOptionPane.showMessageDialog(frame, "Nhập data ní ơi !");
	    		}
	    		else {
		    		ClearFilesInHadoop();
	    			if (rdbtn_Content.isSelected()) {
	    				if (hadoopInteractor.createAndPutFile(textInput.getText(), FILE_NAME_INPUT, FOLDER_NAME_INPUT)){
	    					Multifile.add("/" + FOLDER_NAME_INPUT);
	    				}else{
	    					JOptionPane.showMessageDialog(frame, "Tạo data thất bại rồi ní ơi !");
	    					return;
	    				}
	    			}else {
	    				String srcString = inputFile.getText();
	    				if (hadoopInteractor.putFile(srcString, FOLDER_NAME_INPUT)){
	    					Multifile.add("/" + FOLDER_NAME_INPUT);
	    				}else{
	    					JOptionPane.showMessageDialog(frame, "Tạo data thất bại rồi ní ơi !");
	    					return;
	    				}
	    			}
	    			
	    			if (Multifile.isEmpty()) {
	    				JOptionPane.showMessageDialog(frame, "Không có File Input ní ơi !");
	    			}else {
	    				for(int i = 0; i < Multifile.size(); i++)
						{ 
							input[i] = new Path(Multifile.get(i));
						}
		    			
		    			try {
		    				if (options.getSelectedItem().toString() == "Word Count"){
		    					MapReduce_WC(input);
				    		}else if (options.getSelectedItem().toString() == "Minimum Spanning Tree")
				    		{
				    			if (rdbtn_Content.isSelected()){
				    				if (FileValidator.isValidDataFile("C:/Users/pc/Desktop/MapReduce_GUI/src/main/java/data/input.txt")){
					    				MapReduce_MST(input);
					    			}else{
					    				JOptionPane.showMessageDialog(frame, "File data không hợp lệ rồi ní ơi !");
					    				return;
					    			}
				    			}else{
				    				if (FileValidator.isValidDataFile(inputFile.getText())){
					    				MapReduce_MST(input);
					    			}else{
					    				JOptionPane.showMessageDialog(frame, "File data không hợp lệ rồi ní ơi !");
					    				return;
					    			}
				    			}
				    			
				    		}		
						}catch (ClassNotFoundException e1) { e1.printStackTrace();} 
						catch (IOException e1) { e1.printStackTrace(); } 
						catch (InterruptedException e1) { e1.printStackTrace(); }
		    			
		    			BufferedReader br = null;
						String kq = "";
						
						try {  
							Path pt = new Path("hdfs://localhost:9000/output/part-r-00000");
							br = new BufferedReader(new InputStreamReader(hadoopInteractor.fileSystem.open(pt)));				 
							String textInALine = br.readLine();
							while (textInALine != null) {		
								kq += textInALine+"\n";
								textInALine = br.readLine();
							}
							textOutput.setText(kq.toString());
							BufferedWriter writer = new BufferedWriter(new FileWriter(hadoopInteractor.FILE_LOCAL_OUTPUT_DIR));
				            writer.write(kq.toString());
				            writer.close();

						} catch (IOException e1) { e1.printStackTrace();
						} finally {
						  try { br.close(); } catch (IOException e1) { e1.printStackTrace(); }
						}
	    			}
	    		}
	    	}
	    });
	}

	
	public void ClearFilesInHadoop(){
		hadoopInteractor.deleteFile(FOLDER_NAME_OUTPUT);
		hadoopInteractor.deleteFile(FOLDER_NAME_INPUT);
		hadoopInteractor.deleteFile("user");
		hadoopInteractor.deleteFile("tmp");
	}
	
	public void MapReduce_WC(Path[] input) throws IOException, ClassNotFoundException, InterruptedException{
			
			Path output = new Path("/"+ FOLDER_NAME_OUTPUT);
			
			Job job = Job.getInstance(hadoopInteractor.conf, CLASS_NAME);
			
			job.setJarByClass(Main.class);
			job.setMapperClass(WordCountMapper.class);
			job.setReducerClass(WordCountReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			
			
			for (int i = 0; i < Multifile.size(); i++) {
				FileInputFormat.addInputPath(job, input[i]);
			}
			
			FileOutputFormat.setOutputPath(job, output);
			job.waitForCompletion(true);
		}
	
	public void MapReduce_MST(Path[] input) throws IOException, ClassNotFoundException, InterruptedException{
		Path output = new Path("/"+ FOLDER_NAME_OUTPUT);
		Job job = Job.getInstance(hadoopInteractor.conf, CLASS_NAME);
		
		job.setJarByClass(Main.class);
		job.setMapperClass(MST_Mapper.class);
		job.setReducerClass(MST_Reducer.class);
		
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
			
		for (int i = 0; i < Multifile.size(); i++) {
			FileInputFormat.addInputPath(job, input[i]);
		}
		
		FileOutputFormat.setOutputPath(job, output);
		job.waitForCompletion(true);
	}
	
	public static class MST_Mapper extends Mapper<LongWritable, Text, IntWritable, Text>{
		private boolean isFirstLine = true;
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
			String text = value.toString();
			
			if (isFirstLine) {
	            int numberOfVertices = Integer.parseInt(text.trim());
	            hadoopInteractor.conf.setInt("numberV", numberOfVertices);
	            isFirstLine = false;
	            return;
	        }
			
			String[] words = text.split(" ");
			
			if (words.length == 3) {
				String W = words[0];
				String V1 = words[1];
				String V2 = words[2];
				
				IntWritable outputKey = new IntWritable(Integer.parseInt(W));
				Text outputValue = new Text(V1 + "," + V2);
				context.write(outputKey, outputValue);
			}
		}
	}
	
	public static class MST_Reducer extends Reducer<IntWritable, Text, Text, IntWritable>{
		private int numberV = 0;
		private Kruskal kruskal;
		
		public class Kruskal{
			public LinkedList<LinkedList<String>> forest = new LinkedList<LinkedList<String>>();
			public LinkedList<Edge> listEdges = new LinkedList<Main.MST_Reducer.Edge>();
			
			public int getNumberEgde(){
				return listEdges.size();
			}
			
			public String toString(){
		        String output = "";
		        if (this.forest.isEmpty()){output += "{}";}
		        else{
		            for(LinkedList<String> tree : forest){
		                output += "{";
		                for (String V : tree){
		                    output += V;
		                    if (tree.get(tree.size() - 1).equals(V)){
		                        continue;
		                    }else{
		                        output +=",";
		                    }
		                }
		                output += "}";
		            }
		        }
		        output += "\n";
		        return output;
		    }
			
			public void printListEdge(){
		        for (Edge e : this.listEdges){
		            System.out.println(e);
		        }
		    }
			
			public void AddEdge(Edge E){
				this.listEdges.add(E);
			}
			
			public void updateForest(Edge E){
				if (isSuitableEdge(E)){
	                if (isUnique(E)) {
	                    forest.add(E.getTwoV());
	                }else{
	                    remixForest(E);
	                }
	                listEdges.add(E);
	            }else{
	                return;
	            }
		    }
			
			public void remixForest(Edge E){
		        String v1 = E.getLeftV();
		        String v2 = E.getRightV();

		        LinkedList<String> tree1 = null;
		        LinkedList<String> tree2 = null;

		        for (LinkedList<String> tree: forest){
		            if (tree.contains(v1)){
		                tree1 = tree;
		            }
		            if (tree.contains(v2)){
		                tree2 = tree;
		            }
		        }

		        if (tree1 != null && tree2 == null){
		            tree1.add(v2);
		        }
		        else if (tree1 == null && tree2 != null){
		            tree2.add(v1);
		        }else if(tree1 != null && tree2 != null && tree1 != tree2){
		            tree1.addAll(tree2);
		            forest.remove(tree2);
		        }
		    }
			
			public boolean isUnique(Edge E){
				for (LinkedList<String> tree : forest) {
					if (tree.contains(E.getLeftV()) || tree.contains(E.getRightV())) {
						return false;
					}
				}
				return true;
			}
			
			public boolean isSuitableEdge(Edge E){
				for (LinkedList<String> tree : forest) {
					if (tree.contains(E.getLeftV()) && tree.contains(E.getRightV())){
						return false;
					}
				}
				return true;
			}
		}

		public class Edge{
			public String V1, V2;
			public int weight;
			
		    public String toString(){
		        return "(" + V1 + "," + V2 + ") : " + weight; 
		    }
		    
		    public String printEdge(){
		    	return "(" + V1 + "," + V2 + ") : ";
		    }
		    
		    public Edge(int W, String V1, String V2){
		        this.V1 = V1;
		        this.V2 = V2;
		        this.weight = W;
		    }
			
			public int getW(){
				return this.weight;
			}
			
			public String getLeftV() {return V1;}
			public String getRightV() {return V2;}
			
			public LinkedList<String> getTwoV(){
				LinkedList<String> temp = new LinkedList<String>();
				temp.add(V1);
				temp.add(V2);
				return temp;
			}
		}
		
		protected void setup(Context context) throws IOException, InterruptedException {
	        kruskal = new Kruskal();
	        this.numberV = hadoopInteractor.conf.getInt("numberV", 0);
	        System.out.println("Số đỉnh: " + numberV);
	    }
		
	    public void reduce(IntWritable key, Iterable<Text> values, Context con) throws IOException, InterruptedException {
	    	String[] Vs;
	    	for (Text value : values) {
	    		if (kruskal.getNumberEgde() < numberV - 1) {
	    			Vs = value.toString().split(",");
		    		Edge e = new Edge(key.get(), Vs[0], Vs[1]);
		    		if (kruskal.isSuitableEdge(e)) {
						kruskal.updateForest(e);
						Text edge = new Text(e.printEdge()); 
						IntWritable weight = new IntWritable(e.getW());
						con.write(edge, weight);
					}
	    		}else {
					break;
				}
	    	}
	    }
	}
	
	public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
		public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException{
			String text = value.toString();
			String[] words = text.split("[ ,.?!()\r\\n\"\\']");
			
			for (String word : words) {
				Text outputKey = new Text(word.toLowerCase().trim());
				IntWritable outputValue = new IntWritable(1);
				con.write(outputKey, outputValue);
			}
		}
	}
	
	public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
		public void reduce(Text word, Iterable<IntWritable> values, Context con) throws IOException, InterruptedException {
			
			int sum = 0;
			for (IntWritable value : values){
				sum += value.get();
			}
			con.write(word, new IntWritable(sum));
		}
	}
}
