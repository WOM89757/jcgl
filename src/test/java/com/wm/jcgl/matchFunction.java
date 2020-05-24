package com.wm.jcgl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wm.jcgl.entity.*;
import com.wm.jcgl.service.BackService;
import com.wm.jcgl.service.LackService;
import com.wm.jcgl.service.MatchService;
import com.wm.jcgl.service.ResultService;
import com.wm.sys.common.WebUtils;
import com.wm.sys.entity.User;
import com.wm.sys.service.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class matchFunction {
    @Resource
    private MatchService matchService;
    @Resource
    private DeptService deptService;
    @Resource
    private ResultService resultService;
    @Resource
    private LackService lackService;
    @Resource
    private BackService backService;

    @Test
    public void getMatchResult() {


//        SELECT dept_id,book_id,lNum AS number
//        FROM b_match
//        WHERE order_id=5 AND type=1;
        //查询书目缺少详细信息
        QueryWrapper<Match> lqueryWrapper = new QueryWrapper<>();
        lqueryWrapper.select("order_id","dept_id", "book_id", "lNum AS number");
        lqueryWrapper.eq("order_id", 5);
        lqueryWrapper.eq("type", 1);   //类型为缺少
        List<Match> lMatchList = this.matchService.list(lqueryWrapper);
        Iterator<Match> lIterator = lMatchList.iterator();

        // 查询书目剩余详细信息
        QueryWrapper<Match> bqueryWrapper = new QueryWrapper<>();
        bqueryWrapper.select("order_id","dept_id", "book_id", "bNum AS number");
        bqueryWrapper.eq("order_id", 5);
        bqueryWrapper.eq("type", 0);   //类型为剩余
        List<Match> bMatchList = this.matchService.list(bqueryWrapper);
        Iterator<Match> bIterator = bMatchList.iterator();

        //获取操作用户
        User user = (User) WebUtils.getSession().getAttribute("user");
        List<Lack> lList = new ArrayList<Lack>();
        List<Back> bList = new ArrayList<Back>();
        List<Result> resultsList = new ArrayList<Result>();
        while(lIterator.hasNext()){
            Match l_info = lIterator.next();
            // 没有退货信息 缺货信息加入缺货单
            if(bMatchList.size()==0){
                Lack lack = new Lack();
                lack.setBookId(l_info.getBookId());
                lack.setOrderId(l_info.getOrderId());
                lack.setDeptId(l_info.getDeptId());
                lack.setNumber(l_info.getNumber());
//                lack.setOperName(user.getName());
                lack.setCreateTime(new Date());
                lList.add(lack);
                lIterator.remove();
            }
            //有退货信息，根据退货信息匹配缺货信息
            while(bIterator.hasNext()){
                Match b_info = bIterator.next();
                if(b_info.getBookId() == l_info.getBookId()){
                    //if(b_info.num==0){ continue; //该条配货记录已无货，结束此次循环，进行下一次while循环}
                    Integer remainNum = b_info.getNumber() - l_info.getNumber();
                    if(remainNum > 0 || remainNum == 0){
                        Result result = new Result();
                        result.setOrderId(l_info.getOrderId());
                        result.setBdeptId(b_info.getDeptId());
                        result.setLdeptId(l_info.getDeptId());
                        result.setBookId(l_info.getBookId());
                        result.setNumber(b_info.getNumber()-remainNum);
                        resultsList.add(result);
                        lIterator.remove();
                        b_info.setNumber(remainNum);
                        if(remainNum==0){	bIterator.remove(); } //移除数量为零的退货信息
                        break;      //该条缺货记录已匹配完成，结束循环，跳出循环体，匹配下一条缺货数据
                    }
                    else{
                        if(b_info.getNumber()==0){ bIterator.remove(); break;}
                        Result result = new Result();
                        result.setOrderId(l_info.getOrderId());
                        result.setBdeptId(b_info.getDeptId());
                        result.setLdeptId(l_info.getDeptId());
                        result.setBookId(l_info.getBookId());
                        result.setNumber(b_info.getNumber());
                        resultsList.add(result);
                        l_info.setNumber(l_info.getNumber()-b_info.getNumber());
                        bIterator.remove();//移除数量为零的退货信息
                        continue;
                    }
                }
            }
            // 没有可以匹配的退货信息 缺货信息加入缺货单
            if(!bIterator.hasNext()){
                Lack lack = new Lack();
                lack.setBookId(l_info.getBookId());
                lack.setOrderId(l_info.getOrderId());
                lack.setDeptId(l_info.getDeptId());
                lack.setNumber(l_info.getNumber());
//                lack.setOperName(user.getName());
                lack.setCreateTime(new Date());
                lList.add(lack);
                lIterator.remove();
            }//三种状态分析

        }
        if(bMatchList.size()>0) {
            //缺数小于余数 资源剩余  退货信息加入退货单
            Iterator<Match> iterator = bMatchList.iterator();
            while(iterator.hasNext()) {
                Match b_info = iterator.next();
                Back back = new Back();
                back.setBookId(b_info.getBookId());
                back.setOrderId(b_info.getOrderId());
                back.setDeptId(b_info.getDeptId());
                back.setNumber(b_info.getNumber());
//                back.setOperName(user.getName());
                back.setCreateTime(new Date());
                bList.add(back);
                iterator.remove();
            }
        }
        System.out.println(resultsList);
        System.out.println(lList);
        System.out.println(bList);
        if(resultsList.size()>0){
            this.resultService.saveBatch(resultsList);
        }
        if(lList.size()>0){
            this.lackService.saveBatch(lList);
        }
        if(bList.size()>0){
            this.backService.saveBatch(bList);
        }
//        List<Line> list = new ArrayList<Line>();
//        for (Match match : lmatchList) {
//            //根据id查询学校名
//            if (null != match.getDeptId()) {
//                Dept dept = this.deptService.getById(match.getDeptId());
//                if (null != dept) {
//                    match.setSchoolname(dept.getTitle());
//                }
//            }
//            list.add(new Line(match.getSchoolname(), match.getLNum(), match.getBNum()));
//
//        }


//        String name[] = new String[list.size()];
//        Integer lNum[] = new Integer[list.size()];
//        Integer bNum[] = new Integer[list.size()];
//
//        for (int i = 0; i < list.size(); i++) {
//            Line l = list.get(i);
//            name[i] = l.getName();
//            lNum[i] = l.getLNum();
//            bNum[i] = l.getBNum();
//
//        }
//        System.out.println(name.toString());



    }
}