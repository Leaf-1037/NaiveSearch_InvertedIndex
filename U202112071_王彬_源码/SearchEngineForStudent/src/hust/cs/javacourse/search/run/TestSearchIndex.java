package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.util.List;
import java.util.Scanner;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args){
        Sort simpleSorter = new SimpleSorter();
        String indexFile = Config.INDEX_DIR + "index.dat";
        AbstractIndexSearcher searcher = new IndexSearcher();   // 搜索器
        searcher.open(indexFile);
        System.out.println(" Press '#' for ending.       ");
        Scanner input=new Scanner(System.in);
        String word=input.nextLine();   // word是总单词存取
        StringSplitter splitter=new StringSplitter();   //单词分割
        splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
        while(!word.equals("#")) {
            List<String> term= splitter.splitByRegex(word);
            AbstractHit[] hits;
            if(term.size()==1) {
                hits = searcher.search(new Term(word), simpleSorter);
            }
            else if(term.size()==3&&term.get(1).equals("OR")){
                //System.out.println("We get ORRRRRRRR!\n");
                // 调用“或”查询
                hits = searcher.search(new Term(term.get(0)),new Term(term.get(2)),simpleSorter, AbstractIndexSearcher.LogicalCombination.OR);
            }
            else if(term.size()==3&&term.get(1).equals("AND")){
               // System.out.println("We get ANDDDDDDD!\n");
                // 调用“和”查询
                hits = searcher.search(new Term(term.get(0)), new Term(term.get(2)), simpleSorter, AbstractIndexSearcher.LogicalCombination.AND);
            }
            else{
                //System.out.println("We get ANDDDDDDD2!\n");
                // 调用“和”查询
                hits = searcher.search(new Term(term.get(0)), new Term(term.get(1)), simpleSorter, AbstractIndexSearcher.LogicalCombination.AND);
            }
            for (AbstractHit hit : hits) {
                // 将结果依次打印
                System.out.println(hit);
            }
            word=input.next();
        }

    }
}
