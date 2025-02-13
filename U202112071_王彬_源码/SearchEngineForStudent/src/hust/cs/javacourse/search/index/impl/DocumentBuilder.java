package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;

import java.io.*;

public class DocumentBuilder extends AbstractDocumentBuilder {
    /**
     * <pre>
     * 由解析文本文档得到的TermTupleStream,构造Document对象.
     * @param docId             : 文档id
     * @param docPath           : 文档绝对路径
     * @param termTupleStream   : 文档对应的TermTupleStream
     * @return ：Document对象
     * </pre>
     */
    public AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream){
        AbstractDocument doc=new Document(docId,docPath);
        AbstractTermTuple tup=termTupleStream.next();
        while (tup!=null){
            doc.addTuple(tup);
            tup=termTupleStream.next();
        }
        return doc;
    }


    /**
     * <pre>
     * 由给定的File,构造Document对象.
     * 该方法利用输入参数file构造出AbstractTermTupleStream子类对象后,内部调用
     *      AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     * @param file      : 文档对应File对象
     * @return          : Document对象
     * </pre>
     */
    public AbstractDocument build(int docId, String docPath, File file){
        AbstractTermTupleStream doc=null;
        // doc是file中输入经过过滤的结果
        try{
            doc = new TermTupleScanner(
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(file))));
            doc = new LengthTermTupleFilter(doc);
            doc = new PatternTermTupleFilter(doc);
            doc = new StopWordTermTupleFilter(doc);
        }
        catch (FileNotFoundException|SecurityException e){
            e.printStackTrace();
        }
        assert doc !=null;
        return this.build(docId,docPath,doc);
    }

}
