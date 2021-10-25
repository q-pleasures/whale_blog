package com.lq.artifact.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lq.artifact.blog.dao.BlogDao;
import com.lq.artifact.blog.dto.BlogDTO;
import com.lq.artifact.blog.entity.Blog;
import com.lq.artifact.blog.entity.BlogSort;
import com.lq.artifact.blog.entity.BlogContent;
import com.lq.artifact.blog.service.BlogService;
import com.lq.artifact.blog.service.BlogSortService;
import com.lq.artifact.blog.service.EssayContentService;
import com.lq.artifact.blog.vo.BlogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Blog)表服务实现类
 *
 * @author makejava
 * @since 2021-10-25 22:28:42
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, Blog> implements BlogService {
    @Resource
    private BlogSortService blogSortService;
    @Resource
    private EssayContentService essayContentService;


    public List<BlogVO> listAll() {
        final List<Blog> list = baseMapper.selectList(new QueryWrapper<>());
        List<BlogVO> blogVOList = new ArrayList<>();
        list.stream().forEach(data -> {
            final BlogSort byId = blogSortService.getById(data.getClassId());
            final BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(data, blogVO);
            blogVO.setClassName(byId.getName());
            blogVOList.add(blogVO);
        });
        return blogVOList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateContent(BlogDTO blogDTO) {
        Blog essay = new Blog();
        BeanUtils.copyProperties(blogDTO,essay);
        final int i = baseMapper.updateById(essay);
        BlogContent blogContent = new BlogContent();
        blogContent.setId(blogDTO.getId());
        blogContent.setContent(blogDTO.getContent());
        essayContentService.updateById(blogContent);
        return i;
    }

    @Override
    public int saveEssay(BlogDTO blogDTO) {
        Blog essay = new Blog();
        BeanUtils.copyProperties(blogDTO,essay);
        final int i = baseMapper.updateById(essay);
        BlogContent blogContent = new BlogContent();
        blogContent.setId(blogDTO.getId());
        blogContent.setContent(blogDTO.getContent());
        essayContentService.save(blogContent);
        return i;
    }

    @Override
    public boolean save(Blog entity) {
        return super.save(entity);
    }
}
