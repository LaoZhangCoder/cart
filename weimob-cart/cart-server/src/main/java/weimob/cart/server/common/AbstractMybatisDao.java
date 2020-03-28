package weimob.cart.server.common;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @Author: 老张
 * @Date: 2020/3/26
 */
public abstract class AbstractMybatisDao<T> {
    @Autowired
    protected SqlSession sqlSession;

    public final String nameSpace;

    public AbstractMybatisDao() {
        if (this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            this.nameSpace = ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
        } else {
            this.nameSpace = ((Class)((ParameterizedType)this.getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
        }

    }

    public Boolean create(T t) {
        return this.sqlSession.insert(this.sqlId("create"), t) == 1;
    }

    public Integer creates(List<T> ts) {
        return this.sqlSession.insert(this.sqlId("creates"), ts);
    }

    public Integer creates(T t0, T t1, T... tn) {
        return this.sqlSession.insert(this.sqlId("creates"), Arrays.asList(t0, t1, tn));
    }

    public Boolean delete(Long id) {
        return this.sqlSession.delete(this.sqlId("delete"), id) == 1;
    }

    public Integer deletes(List<Long> ids) {
        return this.sqlSession.delete(this.sqlId("deletes"), ids);
    }

    public Integer deletes(Long id0, Long id1, Long... idn) {
        return this.sqlSession.delete(this.sqlId("deletes"), Arrays.asList(id0, id1, idn));
    }

    public Boolean update(T t) {
        return this.sqlSession.update(this.sqlId("update"), t) == 1;
    }

    public T findById(Integer id) {
        return this.findById((long)id);
    }

    public T findById(Long id) {
        return this.sqlSession.selectOne(this.sqlId("findById"), id);
    }

    public T findByUniqueIndex(Map<String, Object> criteria) {
        return this.sqlSession.selectOne(this.sqlId("findByUniqueIndex"), criteria);
    }

    public List<T> findByIds(List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : this.sqlSession.selectList(this.sqlId("findByIds"), ids);
    }

    public List<T> findByIds(Long id0, Long id1, Long... idn) {
        return this.sqlSession.selectList(this.sqlId("findByIds"), Arrays.asList(id0, id1, idn));
    }

    public List<T> listAll() {
        return this.sqlSession.selectList(this.sqlId("list"));
    }


    public List<T> list(Map<?, ?> criteria) {
        return this.sqlSession.selectList(this.sqlId("list"), criteria);
    }

    protected String sqlId(String id) {
        return this.nameSpace + "." + id;
    }

    protected SqlSession getSqlSession() {
        return this.sqlSession;
    }
}
