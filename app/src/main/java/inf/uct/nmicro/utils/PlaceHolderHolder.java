package inf.uct.nmicro.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;
import java.util.Random;

import inf.uct.nmicro.R;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class PlaceHolderHolder extends TreeNode.BaseNodeViewHolder<PlaceHolderHolder.PlaceItem> {


    public PlaceHolderHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, PlaceItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_place_node, null, false);


        //TextView placeName = (TextView) view.findViewById(R.id.place_name);
        //placeName.setText(value.name);

        final PrintView iconView = (PrintView) view.findViewById(R.id.like);
        iconView.setImageDrawable(value.icon);

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }


    public static class PlaceItem {
        public String name;
        public Drawable icon;

        public PlaceItem(String name) {
            this.name = name;
        }
        public PlaceItem(String name, Drawable icon) {
            this.name = name;
            this.icon = icon;
        }
        // rest will be hardcoded
    }

}
