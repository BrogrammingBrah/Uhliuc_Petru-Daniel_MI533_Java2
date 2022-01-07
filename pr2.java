
import java.awt.*;
import java.net.*;
import java.io.*;
class pr2 extends Frame {     
    String path, dir;
    URL simFile;            
    String[] data;
public static void main (String args[]){new pr2();}
int q1, q2, q3, k, r,processes_count,start,nrfaze,cpu,io,rpt;
	String name;
	Phase faze[];
	Proces proc[];
	static long time=0;
	String Q1="", Q2="", Q3="", QIO="";
	boolean finished=false;
	Proces proces_curent=null,IO_proc=null;
	char alias='-', alias2='-';
pr2(){
    Dimension dim=getToolkit().getScreenSize();
    setBackground(new Color(38, 104, 165));
    setForeground(new Color(255,255,0));    
    setResizable(false);       
    AddMenuBar(); 
    setTitle("Window");                  
    resize(500,500);
    move((int)((dim.width-500)/2),(int)((dim.height-500)/2));
    show();     	
}
void AddMenuBar(){
	MenuBar meniul=new MenuBar();
	Menu f= new Menu("File");
	f.add("Open");
	f.add("-");
	f.add("Exit");
	meniul.add(f);
	setMenuBar(meniul);	
}
public boolean handleEvent(Event e){
		if(e.id==Event.WINDOW_DESTROY){
		  System.exit(0);
		  return true;
		  }
		  else if(e.id==Event.ACTION_EVENT && e.target instanceof MenuItem){
		     if("Exit".equals(e.arg)){
			   System.exit(0);
			   return true;
	} 
	else if("Open".equals(e.arg)){
		loadFile();
		return true;
	}
	else return true;
}else return false;
}
void loadFile(){
	try{
		FileDialog fd=new FileDialog(this,"Open *.sim File",0);
		if(dir!=null) fd.setDirectory(dir);
		fd.setVisible(true);
		if(fd.getFile()!=null){
			dir=fd.getDirectory();
			String fisier=fd.getFile();
			path=dir+fisier;
			try{simFile=new URL("file:/"+path);}
			catch(MalformedURLException e){}
			setTitle("Waiting...");
			readDataInput(simFile);
			writeDataOutput();
			openDataOutput();
			setTitle("Finished");
		}
	}
	catch(Exception e){}
}
FileWriter fW;
BufferedWriter bw;  
void writeFile(File f) throws Exception {
    if (path!="") {
        if(f.exists()) f.delete();
        FileWriter fW = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fW);
        for (int i = 0; i < data.length; i++) bw.write(modific(data[i]) + "\n");
        bw.flush();
        bw.close();
    }
}
String modific(String s){return "zzz" + s;}
void readDataInput(URL simF){		
		String line = "";
    		try {
				InputStream is = simF.openStream();
        		DataInputStream dtnpstr = new DataInputStream(new BufferedInputStream(is));
        		try {	
            			while ((line = dtnpstr.readLine()) != null) 
						{
            				
            				if(line.startsWith("SIM_CONDITIONS"))
							{
            					line = line.substring(line.indexOf('=')+1);
		    					try{q1 = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
	        					catch(NumberFormatException e){}
								line = line.substring(line.indexOf('=')+1);
			   					try{q2 = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
	        					catch(NumberFormatException e){}   
								line = line.substring(line.indexOf('=')+1);
		      					try{q3 = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
	        					catch(NumberFormatException e){} 
								line = line.substring(line.indexOf('=')+1);
	        					try{k = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
	        					catch(NumberFormatException e){} 
								line = line.substring(line.indexOf('=')+1);
		     					try{r = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
	        					catch(NumberFormatException e){} 
								continue;
		     				}	
							if(line.startsWith("processes_count"))
							{
            					line = line.substring(line.indexOf('=')+1);
		    					try{processes_count = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
	        					catch(NumberFormatException e){}
								proc=new Proces[processes_count];
								line=dtnpstr.readLine();
								line=dtnpstr.readLine();
								for (int m=0; m<processes_count; m++)
								{
									if(line.startsWith("Process #"+(m+1)))
									{
										line=dtnpstr.readLine();
										name = line.substring(line.indexOf('=')+1);
										line=dtnpstr.readLine();
										line=line.substring(line.indexOf('=')+1);
										try{start = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
										catch(NumberFormatException e){}
									
										line=dtnpstr.readLine();
										line=line.substring(line.indexOf('=')+1);
										try{nrfaze = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
										catch(NumberFormatException e){}
										faze=new Phase[nrfaze];
										for (int i=0; i<nrfaze; i++)
										{
											line=dtnpstr.readLine();
											line = line.substring(line.indexOf('=')+1);
										
											try{cpu = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
											catch(NumberFormatException e){}
											line = line.substring(line.indexOf('=')+1);
											try{io = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
											catch(NumberFormatException e){}   			
											line = line.substring(line.indexOf('=')+1);
											try{rpt = Integer.parseInt(line.substring(0,line.indexOf(' ',0)));}
											catch(NumberFormatException e){} 
											faze[i]=new Phase(cpu,io,rpt);
										}
									proc[m]=new Proces(name,(char)(65+m),start,nrfaze,faze);
									line=dtnpstr.readLine();
									line=dtnpstr.readLine();
									}								
								}
								continue;
							}
            			}
	      		}
        		catch (EOFException e) {}	
        		is.close();	
    		} 
    		catch (IOException e) {}
} 
void writeDataOutput(){
	try{
			fW = new FileWriter(new File("output.htm"));
			bw = new BufferedWriter(fW);   
			bw.write("<html><head><title>Proiect_simulator</title></head><body BACKGROUND=\"bkg.jpg\" BGPROPERTIES=\"fixed\"><center>");
			bw.write("<a name='top'></a><br><br>");
			bw.write("<h1>THREADS SIMULATION</h1><br>");			
			bw.write("<table width=30%><tr><td><a href='#simind'>Simulation input Data</a></td></tr><tr><td><a href='#procdata'>Processes Data</a></td><td><a href='#p1'>Proces #1</a><br><a href='#p2'>Proces #2</a><br><a href='#p3'>Proces #3</a><br><a href='#p4'>Proces #4</a><br><a href='#p5'>Proces #5</a><br></td></tr><tr><td><a href='#simoutdata'>Simulation Output Data</a><td/><td>&nbsp;<td/></tr></table>");
			bw.write("<br><br><br><br>");
			bw.write("<a name='simind'></a>");
			bw.write("<div align='left'><h3>SIMULATION INPUT DATA</h3>");
			bw.write("<table width=100% border='2' ><tr align='center'><td><b>MAX PRIORITY</b></td><td><b>NORMAL PRIORITY</b></td><td><b>MIN PRIORITY</b></td><td><b>PENALTY LIMIT</b></td><td><b>AWARD LIMIT</b></td></tr><tr align='center'><td>q1 = "+q1+"</td><td>q2 = "+q2+"</td><td>q3 = "+q3+"</td><td>k = "+k+"</td><td>r = "+r+"</td></tr></table>");
			bw.write("<div align='left'><a href='#top'>top</a>");
			bw.write("<br><br><br><br>");
			bw.write("<div align='left'><a name='procdata'></a>");
			bw.write("<h4>PROCESSES DATA</h4>");
			bw.write("<div align='left'>Processes_Count = "+processes_count+"</div>");
			for (int i=0; i<processes_count;i++)
			{
				bw.write("<div align='left'><a name='p"+(i+1)+"'></a>");
				bw.write("<h4>PROCESS #"+(i+1)+"</h4>");
				bw.write("<table width=100% border='2' >");
				bw.write("<tr align='center'>");
				bw.write("<th width=20%>NAME</th>");
				bw.write("<th width=20%>ALIAS</th>");
				bw.write("<th width=20%>START TIME</th>");
				bw.write("<th width=20%>PHASES COUNT</th>");
				bw.write("</tr> ");
				bw.write("<tr align='center'>");
				bw.write("<td>"+proc[i].NAME+"</td>");
				bw.write("<td>"+proc[i].ALIAS+"</td>");
				bw.write("<td>"+proc[i].startime+"</td>");
				bw.write("<td>"+proc[i].PHASES_COUNT+"</td>");
				bw.write("</tr> ");
				bw.write("<tr align='center'>");
				bw.write("<th width=20%>PHASE COUNT</th>");
				bw.write("<th width=20%>CPU TIMES COUNT</th>");
				bw.write("<th width=20%>I/O TIMES COUNT</th>");
				bw.write("<th width=20%>REPEAT COUNT</th>");
				bw.write("</tr> ");
				for (int j=0; j<proc[i].PHASES_COUNT;j++)
				{
					bw.write("<tr align='center'>");
					bw.write("<td>"+(j+1)+"</td>");
					bw.write("<td>"+proc[i].phases[j].CPU_TIMES_COUNT+"</td>");
					bw.write("<td>"+proc[i].phases[j].IO_TIMES_COUNT+"</td>");
					bw.write("<td>"+proc[i].phases[j].REPEAT_COUNT+"</td>");
					bw.write("</tr>");
				}
				bw.write("</table>");
				bw.write("<br><br>");
				bw.write("<div align='left'><a href='#top'>top</a>");
			}
				bw.write("<br><br><br><br>");
				bw.write("<a name='simoutdata'></a>");
				bw.write("<h4>SIMULATION OUTPUT DATA</h4>");
				bw.write("<table width=100% border='2' >");
				bw.write("<tr align='center'>");
				bw.write("<th width=7%>TIME</th>");
				bw.write("<th width=7%>CPU</th>");
				bw.write("<th width=7%>I/O</th>");
				bw.write("<th width=12%>Q1 Queue</th>");
				bw.write("<th width=12%>Q2 Queue</th>");
				bw.write("<th width=12%>Q3 Queue</th>");
				bw.write("<th width=12%>I/O Queue</th>");
				bw.write("</tr> ");
				proiect();
				bw.write("</table>");
			bw.write("</body></html>");
			bw.flush();
			bw.close();	        	
        	}
		catch(IOException e){}
	}
void proiect()throws IOException
	{
		int max=0,ct=0,ct2=0,i,qtc2=0,qtc3=0;
		String addQ="",addQ2="",addQ3="";
		boolean set=false;
		for (i=0;i<processes_count;i++)
			if(proc[i].startime>max)
				max=proc[i].startime;
		for (i=0;i<processes_count;i++)
			if(proc[i].startime==0)
				Q1+=proc[i].ALIAS;
		while (!finished)
		{
			if (time<=max&&time>0)
				for (i=0;i<processes_count;i++)
					if(proc[i].startime==time)
						addQ+=proc[i].ALIAS;
			if (Q1.compareTo("")!=0||ct!=0)
			{
			if (qtc2<q2&&qtc2!=0) {Q2=alias+Q2; qtc2=0;}
			  if (qtc3<q3&&qtc3!=0) {Q3=alias+Q3; qtc3=0;}
				if (ct==0)
				{
					alias=Q1.charAt(0);
					proces_curent=daProces(alias);
					Q1=Q1.substring(1);
				}			
					if (proces_curent.PHASES_COUNT!=proces_curent.faza_curenta)
					{
						proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT--;	
					}
					else
					{
						write_on_rand(time,alias,alias2,Q1,Q2,Q3,QIO);
						time++;
						ct=0;
						proces_curent.finished=true;
						bw.write("<tr><td colspan=7 bgcolor='#ff0000' align='center'>Process "+((int)(alias)-64)+" is finished.</td></tr>");
						finished=test();
						continue;
					}
				write_on_rand(time,alias,alias2,Q1,Q2,Q3,QIO);
				if (IO_proc!=null)
				{
					if (ct2<IO_proc.phases[IO_proc.faza_curenta].IO_TIMES_COUNT)
						ct2++;
					if (IO_proc.phases[IO_proc.faza_curenta].IO_TIMES_COUNT==ct2)
					{
						switch (IO_proc.from)
							{
								case 1:	addQ+=IO_proc.ALIAS; break;
								case 2: Q2+=IO_proc.ALIAS; break;
								case 3: Q3+=IO_proc.ALIAS; break;
							}
						if (IO_proc.phases[IO_proc.faza_curenta].REPEAT_COUNT==0)
							{
								IO_proc.faza_curenta++;
								bw.write("<tr><td colspan=7 bgcolor='#ffff00' align='center'>Phase #"+IO_proc.faza_curenta+" of the Process #"+((int)(alias2)-64)+" is finished</td></tr>");
							}	
						ct2=0;
						IO_proc=null;
						alias2='-';
						if (QIO.compareTo("")!=0)
						{
							alias2=QIO.charAt(0);
							QIO=QIO.substring(1);
							IO_proc=daProces(alias2);
							set=true;
						}
					}
				}	
					if (proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT==0)
					{
						proces_curent.phases[proces_curent.faza_curenta].REPEAT_COUNT--;
						proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT=proces_curent.phases[proces_curent.faza_curenta].cpu_bak;
						proces_curent.award++;
						QIO+=alias;
						daProces(alias).from=1;	
						if (!set)
						{
							if (ct2==0)
							{
								alias2=QIO.charAt(0);
								QIO=QIO.substring(1);
								IO_proc=daProces(alias2);
							}
						}
						set=false;
						ct=0; qtc2=0;qtc3=0;
						if (addQ.length()>1)
							addQ=Qsort(addQ);
						Q1+=addQ;
						addQ="";
						time++;
						continue;
					}
				if (ct==q1-1)
				{
					proces_curent.penalty++;
					if(proces_curent.penalty==k)
					{
						Q2+=alias;
						proces_curent.penalty-=k;
						daProces(alias).from=2;
						}
					else
						addQ+=alias;
					ct=0;
				}
				else ct++;
				if (addQ.length()>1)
					addQ=Qsort(addQ);
				Q1+=addQ;
				addQ="";
			}
			else
			{
				if (Q2.compareTo("")!=0||qtc2!=0)
				{
					if (qtc3<q3&&qtc3!=0) {Q3=alias+Q3; qtc3=0;}
					if (qtc2==0)
					{
						alias=Q2.charAt(0);
						proces_curent=daProces(alias);
						Q2=Q2.substring(1);
					}
					if (proces_curent.PHASES_COUNT!=proces_curent.faza_curenta)
					{
						proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT--;	
					}
					else
					{
						write_on_rand(time,alias,alias2,Q1,Q2,Q3,QIO);
						time++;
						qtc2=0;
						proces_curent.finished=true;
						bw.write("<tr><td colspan=7 bgcolor='#ff0000' align='center'>Process "+((int)(alias)-64)+" is finished.</td></tr>");
						finished=test();
						continue;
					}
					write_on_rand(time,alias,alias2,Q1,Q2,Q3,QIO);
					if (IO_proc!=null)
					{
						if (ct2<IO_proc.phases[IO_proc.faza_curenta].IO_TIMES_COUNT)
							ct2++;
						if (IO_proc.phases[IO_proc.faza_curenta].IO_TIMES_COUNT==ct2)
						{
							switch (IO_proc.from)
							{
								case 1:	Q1+=IO_proc.ALIAS; break;
								case 2: addQ2+=IO_proc.ALIAS; break;
								case 3: Q3+=IO_proc.ALIAS; break;
							}
							
							if (IO_proc.phases[IO_proc.faza_curenta].REPEAT_COUNT==0)
							{
								IO_proc.faza_curenta++;
								bw.write("<tr><td colspan=7 bgcolor='#ffff00' align='center'>Phase #"+IO_proc.faza_curenta+" of the Process  #"+((int)(alias2)-64)+" is finished.</td></tr>");
							}
							ct2=0;
							IO_proc=null;
							alias2='-';
							if (QIO.compareTo("")!=0)
							{
								alias2=QIO.charAt(0);
								QIO=QIO.substring(1);
								IO_proc=daProces(alias2);
								set=true;
							}
						}
					}
					if (proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT==0)
						{
							proces_curent.phases[proces_curent.faza_curenta].REPEAT_COUNT--;
							proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT=proces_curent.phases[proces_curent.faza_curenta].cpu_bak;
							proces_curent.award++;
							QIO+=alias;
							daProces(alias).from=2;
							if (proces_curent.award==r)
							{
								proces_curent.award-=r;
								proces_curent.from--;
							}
							if (!set)
								{
									if (ct2==0)
									{
										alias2=QIO.charAt(0);
										QIO=QIO.substring(1);
										IO_proc=daProces(alias2);
									}
								}
							set=false;
							qtc2=0;
							qtc3=0;
							if (addQ2.length()>1)
								addQ2=Qsort(addQ2);
							Q2+=addQ2;
							addQ2="";
							time++;
							continue;
						}
					set=false;
					if (qtc2==q2-1)
					{
						proces_curent.penalty++;
						if(proces_curent.penalty==k)
							Q3+=alias;
						else
							addQ2+=alias;
						qtc2=0;
					}
					else qtc2++;
					if (addQ2.length()>1)
						addQ2=Qsort(addQ2);
					Q2+=addQ2;
					addQ2="";
				}
				else
				{
					if (Q3.compareTo("")!=0||qtc3!=0)
					{
						if (qtc3==0)
						{
							alias=Q3.charAt(0);
							proces_curent=daProces(alias);
							Q3=Q3.substring(1);
						}
						if (proces_curent.PHASES_COUNT!=proces_curent.faza_curenta)
						{
							proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT--;	
						}
						else
						{
							write_on_rand(time,alias,alias2,Q1,Q2,Q3,QIO);
							time++;
							qtc3=0;
							proces_curent.finished=true;
							bw.write("<tr><td colspan=7 bgcolor='#ff0000' align='center'>Process "+((int)(alias)-64)+" is finished</td></tr>");
							finished=test();
							continue;
						}
						write_on_rand(time,alias,alias2,Q1,Q2,Q3,QIO);
						if (IO_proc!=null)		
						{
							if (ct2<IO_proc.phases[IO_proc.faza_curenta].IO_TIMES_COUNT)
								ct2++;
							if (IO_proc.phases[IO_proc.faza_curenta].IO_TIMES_COUNT==ct2)
							{
								switch (IO_proc.from)
								{
									case 1:	Q1+=IO_proc.ALIAS; break;
									case 2: Q2+=IO_proc.ALIAS; break;
									case 3: addQ3+=IO_proc.ALIAS; break;
								}
								
								if (IO_proc.phases[IO_proc.faza_curenta].REPEAT_COUNT==0)
								{
									IO_proc.faza_curenta++;
									bw.write("<tr><td colspan=7 bgcolor='#ffff00' align='center'>Phase #"+IO_proc.faza_curenta+" of the Process #"+((int)(alias2)-64)+" is finished</td></tr>");
								}
								ct2=0;
								IO_proc=null;
								alias2='-';
								if (QIO.compareTo("")!=0)
									{
										alias2=QIO.charAt(0);
										QIO=QIO.substring(1);
										IO_proc=daProces(alias2);
										set=true;
									}
							}
						}				
						if (proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT==0)
						{
							proces_curent.phases[proces_curent.faza_curenta].REPEAT_COUNT--;
							proces_curent.phases[proces_curent.faza_curenta].CPU_TIMES_COUNT=proces_curent.phases[proces_curent.faza_curenta].cpu_bak;
							proces_curent.award++;
							QIO+=alias;
							daProces(alias).from=3;
							if (proces_curent.award==r)
							{
								proces_curent.award-=r;
								proces_curent.from--;
							}
							if (!set)
								{
									if (ct2==0)
									{
										alias2=QIO.charAt(0);
										QIO=QIO.substring(1);
										IO_proc=daProces(alias2);
									}
								}
							set=false;
							qtc3=0;
							if (addQ3.length()>1)
								addQ3=Qsort(addQ3);
							Q3+=addQ3;
							addQ3="";
							time++;
							continue;
						}
						set=false;
						if (qtc3==q3-1)
						{
							proces_curent.penalty++;
								addQ3+=alias;
							qtc3=0;
						}
						else qtc3++;
						if (addQ3.length()>1)
							addQ3=Qsort(addQ3);
						Q3+=addQ3;
						addQ3="";
					}
					else
					{
						alias='-';
						write_on_rand(time,alias,alias2,Q1,Q3,Q3,QIO);
						if (IO_proc!=null)
						{
							if (ct2<IO_proc.phases[IO_proc.faza_curenta].IO_TIMES_COUNT)
								ct2++;
							if (IO_proc.phases[IO_proc.faza_curenta].IO_TIMES_COUNT==ct2)
							{
								switch (IO_proc.from)
								{
									case 1:	Q1+=IO_proc.ALIAS; break;
									case 2: Q2+=IO_proc.ALIAS; break;
									case 3: Q3+=IO_proc.ALIAS; break;
								}
								if (IO_proc.phases[IO_proc.faza_curenta].REPEAT_COUNT==0)
								{
									IO_proc.faza_curenta++;
									bw.write("<tr><td colspan=7 bgcolor='#ffff00' align='center'>Phase #"+IO_proc.faza_curenta+" of the Process #"+((int)(alias2)-64)+" </td></tr>");
								}
								ct2=0;
								IO_proc=null;
								alias2='-';
								if (QIO.compareTo("")!=0)
								{
									alias2=QIO.charAt(0);
									QIO=QIO.substring(1);
									IO_proc=daProces(alias2);
								}
							}
						}
					}
				}
			}
			time++;
			finished=test();
		}
		bw.write("<tr align='center'><td colspan='7' BGCOLOR='#FF0000'>Simulation is finished.</td></tr>");
	}
	boolean test()
{
	boolean val=true;
	for (int i=0; i<processes_count;i++)
	{
		if (!proc[i].finished)
		{val=false; break;}
	}
	return val;
}
void write_on_rand(long t, char prCpu, char prIO, String c1, String c2, String c3, String cIO)throws IOException
	{
		String tt="00000"+t;
		tt=tt.substring(tt.length()-6);
		if (c1.compareTo("")==0) c1="-";
		if (c2.compareTo("")==0) c2="-";
		if (c3.compareTo("")==0) c3="-";
		if (cIO.compareTo("")==0) cIO="-";
		bw.write("<tr align='center'>");
		bw.write("<td><font color='#0000ff'>"+tt+"</font></td>");
		if (prCpu!='-')
			bw.write("<td>CPU: <a href=#p"+((int)(prCpu-64))+">"+prCpu+"</a></td>");
		else
			bw.write("<td>CPU: "+prCpu+"</td>\n");
		if (prIO!='-')
			bw.write("<td>I/O: <a href=#p"+((int)(prIO-64))+">"+prIO+"</a></td>");
		else
			bw.write("<td>I/O: "+prIO+"</td>");
		bw.write("<td>"+c1+"</td>");
		bw.write("<td>"+c2+"</td>");
		bw.write("<td>"+c3+"</td>");
		bw.write("<td>"+cIO+"</td>");
		bw.write("</tr> ");
	}
	Proces daProces(char c)
	{
		Proces a=null;
		for (int ii=0; ii<processes_count;ii++)
		{
			if(proc[ii].ALIAS==c) a=proc[ii];
		}
		return a;
	}
	String Qsort(String deOrdonat)
	{	
		char vector[]=new char[10];
		int i=0;
		while (deOrdonat!="")
		{
				vector[i]=deOrdonat.charAt(0);
				if (deOrdonat.length()>1)
					deOrdonat=deOrdonat.substring(1);
				else
					deOrdonat="";
				i++;
		}
		java.util.Arrays.sort(vector,0,i);
		for (int j=0; j<i; j++)
			if (vector[j]!=0)
				deOrdonat+=vector[j];
		return(deOrdonat);
	}
void openDataOutput(){try{
Process pr=Runtime.getRuntime().exec("cmd.exe /C output.htm");
pr.waitFor();
}catch(Exception exception){exception.printStackTrace();}
}	
}
class Phase{
	public int PHASE_COUNT;
	public int CPU_TIMES_COUNT;
	public int IO_TIMES_COUNT;
	public int REPEAT_COUNT;
	public int cpu_bak;
	public Phase(int cpu, int io, int rpt)
	{
		CPU_TIMES_COUNT=cpu;
		IO_TIMES_COUNT=io;
		REPEAT_COUNT=rpt;
		cpu_bak=cpu;
	}
}
class Proces
{
	public String NAME;
	public char ALIAS;
	public int startime;
	public int PHASES_COUNT;
	public Phase []phases;
	public int faza_curenta=0;
	public int penalty=0, award=0;
	private int cpu,io,rpt;
	public int from=0;
	public boolean finished=false;
	public Proces(String nume, char nickname, int start, int nrfaze, Phase []faze)
	{
		NAME=nume;
		ALIAS=nickname;
		startime=start;
		PHASES_COUNT=nrfaze;
		phases=faze;
	}
}