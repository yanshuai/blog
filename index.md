---
layout: base
title: 首页
---
<div id="main-content">
  <div class="container">
    <div class="row">
      {% for post in paginator.posts %}
      <div class="col-md-4">
        <div class="panel">
          <div class="panel-heading">
            <div class="title">
              <a href="http://github.yanshuai.com/blog/{{ post.url }}">{{ post.title }}</a>
            </div>
          </div>
          <div class="panel-body">
            <blockquote>
              <div class="perex">{{ post.perex }}</div>
            </blockquote>
          </div>
          <div class="panel-footer">
            <div class="date">{{ post.date | date: "%b %e, %Y" }}</div>
          </div>
        </div>
      </div>
      {% endfor %}
    </div>
    <div class="pagination">
      {% if paginator.previous_page == 1 %}
      <a href="/" class="previous">Previous</a>, 
      {% elsif paginator.previous_page %}
      <a href="{{ site.baseurl }}/page{{paginator.previous_page}}" class="previous">Previous</a>, 
      {% else %}
      <span>Previous</span>, 
      {% endif %}
      {% if paginator.next_page %}
      <a href="{{ site.baseurl }}/page{{paginator.next_page}}" class="next ">next</a>.
      {% else %}
      <span>next</span>.
      {% endif %}
      <span> Page {{paginator.page}} of {{paginator.total_pages}}.</span>
    </div>
  </div>
</div>
