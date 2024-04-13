package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  AbstractIndexSearcher是检索具体实现的抽象类
 * </pre>
 */
public class IndexSearcher extends AbstractIndexSearcher {
    /**
     * 内存中的索引，子类对象被初始化时为空
     */
    //Index是AbstractIndex的具体实现类，在hust.cs.javacourse.search.index.impl里定义，但没有实现任何覆盖方法，需要学生实现
    //如果学生AbstractIndex的具体实现类类名不是Index，这里需要相应修改

    /**
     * 从指定索引文件打开索引，加载到index对象里. 再执行search方法
     * @param indexFile ：指定索引文件
     */
    public void open(String indexFile){
        this.index.load(new File(indexFile));
        this.index.optimize();
    }

    /**
     * 根据单个检索词进行搜索
     * @param queryTerm ：检索词
     * @param sorter ：排序器
     * @return ：命中结果数组
     */
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter){
        AbstractPostingList postList=index.search(queryTerm);
        if(postList==null) return new AbstractHit[0];   // 如果没有posting list  返回空
        List<AbstractHit> hitList= new ArrayList<>();   // hitList存放结果集
        // 命中列表
        for(int i=0;i<postList.size();i++){
            AbstractPosting post=postList.get(i);
            Map<AbstractTerm,AbstractPosting> maper= new HashMap<>();//词语到posting的映射
            maper.put(queryTerm,post);

            AbstractHit hit=new Hit(post.getDocId(),index.getDocName(post.getDocId()),maper);
            // 创建hit对象以添加
            hit.setScore(sorter.score(hit));
            hitList.add(hit);
        }
        sorter.sort(hitList);   // 对结果进行排序
        return hitList.toArray(new AbstractHit[0]);
    }

    /**
     *
     * 根据二个检索词进行搜索
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter ：    排序器
     * @param combine ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, hust.cs.javacourse.search.query.AbstractIndexSearcher.LogicalCombination combine){
        // 先依次对两个词语进行搜索，之后进行合并操作

        AbstractPostingList postingList1=index.search(queryTerm1);
        AbstractPostingList postingList2=index.search(queryTerm2);

        List<AbstractHit> hitList=new ArrayList<>();

        //任意一个检索词出现在命中文档
        if(combine==LogicalCombination.OR){
            // 由于两个结果集均为有序的，我们考虑归并操作
            int i=0,j=0;    // 分别是postinglist1的指针和postinglist2的指针
            while(i<postingList1.size()&&j<postingList2.size()){
                AbstractPosting tmp1=postingList1.get(i);
                AbstractPosting tmp2=postingList2.get(j);
                AbstractPosting post;
                Map<AbstractTerm,AbstractPosting> map=new HashMap<>();
                //如果两个文档相同，加入其中一个
                if(tmp1.getDocId()==tmp2.getDocId()){
                    post=tmp1;
                    map.put(queryTerm1,tmp1);
                    // 两个下标同时加
                    i++;
                    j++;
                }
                //posting1的ID小，则优先加posting1
                else if(tmp1.getDocId()<tmp2.getDocId()){
                    post=tmp1;
                    map.put(queryTerm1,tmp1);
                    i++;
                }
                //posting2的ID小，则优先加posting2
                else{
                    post=tmp2;
                    map.put(queryTerm2,tmp2);
                    j++;
                }
                AbstractHit hit=new Hit(post.getDocId(),index.getDocName(post.getDocId()),map);
                hit.setScore(sorter.score(hit));
                hitList.add(hit);
            }

            //将余下的文档加入到命中文档
            while(i<postingList1.size()){
                Map<AbstractTerm,AbstractPosting> map=new HashMap<>();
                map.put(queryTerm1,postingList1.get(i));
                AbstractHit hit=new Hit(postingList1.get(i).getDocId(),index.getDocName(postingList1.get(i).getDocId()),map);
                hit.setScore(sorter.score(hit));
                hitList.add(hit);
            }
            while(j<postingList2.size()){
                AbstractPosting tmp=postingList2.get(i);
                Map<AbstractTerm,AbstractPosting> map=new HashMap<>();
                map.put(queryTerm2,tmp);
                AbstractHit hit=new Hit(tmp.getDocId(),index.getDocName(tmp.getDocId()),map);
                hit.setScore(sorter.score(hit));
                hitList.add(hit);
            }
        }

        // AND逻辑两个检索词都必须同时出现在文档中
        else{
            int i=0,j=0;
            while(i<postingList1.size()&&j<postingList2.size()){
                AbstractPosting posting1=postingList1.get(i);
                AbstractPosting posting2=postingList2.get(j);
                if(posting1.getDocId()==posting2.getDocId()){
                    Map<AbstractTerm,AbstractPosting> map=new HashMap<>();
                    map.put(queryTerm1,posting1);//加入其中一个即可
                    AbstractHit hit=new Hit(posting1.getDocId(),index.getDocName(posting1.getDocId()),map);
                    hit.setScore(sorter.score(hit));
                    hitList.add(hit);
                    i++;
                    j++;
                }
                // 若不是一样的直接跳过，增加较小者
                else if(posting1.getDocId()<posting2.getDocId()) i++;
                else j++;
            }
        }
        sorter.sort(hitList);   // 对结果进行排序
        return hitList.toArray(new AbstractHit[0]);
    }

}