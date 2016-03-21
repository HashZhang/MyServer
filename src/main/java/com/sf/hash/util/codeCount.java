package com.sf.hash.util;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 862911 on 2016/1/6.
 */
public class codeCount {
    static long  normalLine=0;
    static long commentLine=0;
    static long whiteLine=0;

    private static void p(Object obj){
        System.out.println(obj);
    }
    private static void countcode(File f) throws Exception {
        BufferedReader br=null;
        BufferedWriter bw=null;
        ArrayList<String> stringList = new ArrayList<String>();
        ArrayList<String> stringList2 = new ArrayList<String>();
        HashMap<String,String> st = new HashMap<String, String>();
        boolean bln=false;
        try{
            br=new BufferedReader(new FileReader(f));
            bw=new BufferedWriter(new FileWriter(new File(f.getPath()+"_modi")));
            String  line="",line2="",key="",value="";;
            try {
                while((line = br.readLine()) != null) {
                    line2=line;
                    line=line.trim();
                    line = line.toLowerCase();
                    if(line.matches("^[\\s&&[^\\n]]*$")){
                        whiteLine+=1;
                    }else if(line.startsWith("/*")&&!line.equals("*/")){
                        commentLine+=1;
                        bln=true;
                    }else if (bln==true){
                        commentLine+=1;
                        if(line.endsWith("*/")){
                            bln=false;
                        }
                    }else if(line.startsWith("/*")&&line.endsWith("*/")){
                        commentLine+=1;
                    }else if(line.startsWith("//")){
                        commentLine+=1;
                    }else if(line.startsWith("--")){
                        commentLine+=1;
                    } else{


                        if(line.startsWith("create")){
                            key = line2.substring(line2.indexOf("`")+1,line2.indexOf("`",line2.indexOf("`")+1));
                            value = "(";
                            stringList.add(line);
                            normalLine+=1;
                        }else if(line.startsWith(")")){
                            value = value.substring(0,value.length()-1)+")";
                            System.out.println(value);
                            st.put(key,value);
                            stringList.add(line);
                            normalLine+=1;
                        }else if(line.startsWith("`")){
                            value += line2.substring(line2.indexOf("`")+1,line2.indexOf("`",line2.indexOf("`")+1))+",";
                            stringList.add(line);
                            normalLine+=1;
                        }else if(line.startsWith("insert")){
                            String ikey = line2.substring(line2.indexOf("`")+1,line2.indexOf("`",line2.indexOf("`")+1));
                            stringList2.add(line2.replace("`"+ikey+"`","`"+ikey+"`"+st.get(ikey)));
                            System.out.println(line2.replace("`"+ikey+"`","`"+ikey+"`"+st.get(ikey)));
                        }
                    }
                }
//                for(String s:stringList){
//                    s.replace("datetime","timestamp");
//                    System.out.println(s);
//                    if(s.startsWith("`")){
//                        s="  "+s;
//                    }
//                    bw.write(s);
//                    bw.newLine();
//                    if(s.endsWith(";"))
//                        bw.newLine();
//                }
                for(String s:stringList2){
                    System.out.println(s);
                    bw.write(s);
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private static void recursivelyFileAccess(String path){
        File f = new File(path);

        File[] fs = f.listFiles();

        if (fs == null) {
            return;
        }

        for (File file : fs) {
            if (file.isFile()) {
                if(file.getName().matches("codeCount.java")){
                    continue;
                }
                if(file.getName().matches(".*\\.sql$")){
                    try {
                        countcode(file);
                    } catch (Exception e) {
                        System.err.println(file.getName()+" has DDL!");
                        e.printStackTrace();
                    }
                }
            } else {
                recursivelyFileAccess(file.getPath());
            }
        }
    }

    public static void main(String args[]) throws Exception {
        recursivelyFileAccess("d:\\user\\862911\\桌面\\压测数据");
        p("注释的代码行数:"+commentLine);
        p("空白的代码行数:"+whiteLine);
        p("有效的代码行数:"+normalLine);
    }

}
