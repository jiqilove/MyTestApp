# MyTestApp
我的各种测试小栗子
## RecyclerView 简单设置


         recyclerViewHelper = new RecyclerViewHelper<T>(context, recyclerView, layout) {

             @Override
 
             public List<T> setDatas() {
 
                 return datas;
             }

             @Override
            public void onBindView(BaseViewHolder myViewHolder, T item, final int i) {
              
        };

       //===========支持线性布局、网格布局=========
        recyclerViewHelper.setLinearLayoutManager(false, false, true);
        //======水平分割线、垂直分割线======== 
        recyclerViewHelper.addItemDecoration();

 ## BindView 绑定
 
 ### 以后会继续添加更多的方法
 
      myViewHolder.setText(id, “str”);
      myViewHolder.setOnClickListener(id, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("cnb", "===" + position);
                    }
                });

