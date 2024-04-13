package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.StopWords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopWordTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public StopWordTermTupleFilter(AbstractTermTupleStream input){
        super(input);
    }

    /**
     * 获得下一个三元组
     * 基于停用词的过滤器
     * @return : 下一个三元组；如果到了流的末尾，返回null
     */
    // 过滤停用词
    @Override
    public AbstractTermTuple next(){
        AbstractTermTuple filter=input.next();
        List<String> stopwords=new ArrayList<String>(Arrays.asList(StopWords.STOP_WORDS));
        while (filter!=null && stopwords.contains(filter.term.getContent()))
            filter=input.next();
        return filter;
    }


}
