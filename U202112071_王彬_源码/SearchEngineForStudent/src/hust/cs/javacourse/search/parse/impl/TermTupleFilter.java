package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public TermTupleFilter(AbstractTermTupleStream input){

        super(input);
    }

    /**
     * 获得下一个三元组
     * 总过滤器
     * @return : 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple filter=input.next();
        // 过滤长度小于三或大于二十的单词
        while(filter!=null&&
                (filter.term.getContent().length()>Config.TERM_FILTER_MAXLENGTH||
                        filter.term.getContent().length()<Config.TERM_FILTER_MINLENGTH)){
            filter=input.next();
        }

        // 先过滤掉非英文字符
        while (filter!=null && !filter.term.getContent().matches(Config.TERM_FILTER_PATTERN))
            filter=input.next();

        // 再过滤含有停用词的单词
        List<String> stopwords=new ArrayList<String>(Arrays.asList(StopWords.STOP_WORDS));
        while (filter!=null && stopwords.contains(filter.term.getContent()))
            filter=input.next();
        return filter;
    }
}
