package iquantex.com.entity.dependent;

import lombok.Data;

import java.util.List;

/**
 * @ClassName DependentList
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/25 8:44 下午
 * @Version 1.0
 */
@Data
public class DependTaskList {

    /**
     * 依赖条件
     */
    private DependentRelation relation = DependentRelation.AND;
    /**
     * 依赖任务
     */
    private List<DependItemList> dependItemList;


}
