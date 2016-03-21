package com.sf.hash.util;


import java.io.*;
import java.util.ArrayList;

/**
 * Created by 862911 on 2016/1/6.
 */
public class codeCount2 {
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
        FileWriter writer = new FileWriter("sql.txt", true);
        stringList.add("**********************************"+f.getName()+"****************************************");
        boolean bln=false;
        try{
            br=new BufferedReader(new FileReader(f));
//            bw=new BufferedWriter(new FileWriter(new File(f.getName()+"!")));
            String  line="",line2="";
            try {
                while((line = br.readLine()) != null) {
                    line2=line;
                    line=line.trim();
                    line = line.toLowerCase();
                    if(line.startsWith("<insert")||line.startsWith("<select")||line.startsWith("<delete")||line.startsWith("<update")) {
                        bln=true;
                        stringList.add("\r\n");
                    }else if(line.startsWith("</insert")||line.startsWith("</select")||line.startsWith("</delete")||line.startsWith("</update")){
                        bln=true;
                        stringList.add("\r\n");
                    } else if(bln){
                        stringList.add(line2);
                    }
                }
                stringList.add("****************************************************************************************");
                for(String s:stringList){
                    System.out.println(s);
                    writer.write(s+"\r\n");
                    if(s.endsWith(";"))
                        bw.newLine();
                }
                writer.flush();
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
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
                if(file.getName().matches(".*\\.xml$")){
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
        recursivelyFileAccess("D:\\Users\\862911\\resource\\resource-core-service\\src\\main\\resources\\sqlmap\\db");
        p("注释的代码行数:"+commentLine);
        p("空白的代码行数:"+whiteLine);
        p("有效的代码行数:"+normalLine);
    }

}
