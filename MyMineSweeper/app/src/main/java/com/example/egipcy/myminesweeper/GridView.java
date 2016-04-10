package com.example.egipcy.myminesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GridView extends View
{
    public GridView(Context c)
    {
        super(c);
        init();
    }

    public GridView(Context c, AttributeSet as)
    {
        super(c, as);
        init();
    }

    // constructor that take in a context, attribute set and also a default
// style in case the view is to be styled in a certian way
    public GridView(Context c, AttributeSet as, int default_style)
    {
        super(c, as, default_style);
        init();
    }

    private void init()
    {
        this.nb_mines = 20;

        this.col_covered = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.col_covered.setColor(0xFF000000);

        this.col_uncovered = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.col_uncovered.setColor(0xFF808080);

        this.col_mined = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.col_mined.setColor(0xFFFF0000);

        this.nb_cells = 10;
        this.grid = new Rect[this.nb_cells][this.nb_cells];
        this.isCovered = new Boolean[this.nb_cells][this.nb_cells];
        this.isMined = init_mines();
        this.numbers_in_cells = init_numbers();

        this.col_M = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.col_M.setColor(0xFF000000);
        this.col_M.setTextSize(70);

        this.size_x = 97;
        this.size_y = 97;

        for (int i = 0; i < this.nb_cells; i++)
            for (int j = 0; j < this.nb_cells; j++)
            {
                this.grid[i][j] = new Rect(i * this.size_x + size_x / 10, j * this.size_y + size_y / 10, (i + 1) * this.size_x, (j + 1) * this.size_y);
                this.isCovered[i][j] = true;
            }


    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        for (int i = 0; i < this.nb_cells; i++)
            for (int j = 0; j < this.nb_cells; j++)
            {
                if (this.isCovered[i][j])
                    canvas.drawRect(this.grid[i][j], this.col_covered);
                else
                {
                    if (this.isMined[i][j])
                    {
                        canvas.drawRect(this.grid[i][j], this.col_mined);
                        canvas.drawText("M", this.grid[i][j].left + 14, this.grid[i][j].top + 72, this.col_M);
                    }
                    else
                    {
                        canvas.drawRect(this.grid[i][j], this.col_uncovered);

                        if (this.numbers_in_cells[i][j] != 0)
                            canvas.drawText(String.valueOf(this.numbers_in_cells[i][j]), this.grid[i][j].left + 26, this.grid[i][j].top + 72, get_col_number(this.numbers_in_cells[i][j]));
                    }
                }
            }
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            invalidate();
            return true;
        }

        if (event.getActionMasked() == MotionEvent.ACTION_UP)
        {
            int pos_x = (int) (event.getX() / this.size_x);
            int pos_y = (int) (event.getY() / this.size_y);

            if (pos_x < this.nb_cells && pos_y < this.nb_cells)
            {
                if (this.isMined[pos_x][pos_y])
                    for (int i = 0; i < this.nb_cells; i++)
                        for (int j = 0; j < this.nb_cells; j++)
                            this.isCovered[i][j] = false;

                else
                    uncover_zero_cells(pos_x, pos_y);
            }

            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    private Boolean[][] init_mines()
    {
        Boolean[][] ret = new Boolean[this.nb_cells][this.nb_cells];

        for (int i = 0; i < this.nb_cells; i++)
            for (int j = 0; j < this.nb_cells; j++)
                ret[i][j] = false;

        int nb_mines = this.nb_mines;
        Random rand = new Random();

        while (nb_mines > 0) {
            int pos = rand.nextInt(this.nb_cells * this.nb_cells);
            if (!ret[pos / this.nb_cells][pos % this.nb_cells])
            {
                ret[pos / this.nb_cells][pos % this.nb_cells] = true;
                nb_mines--;
            }
        }

        return ret;
    }

    private int[][] init_numbers()
    {
        int[][] ret = new int[this.nb_cells][this.nb_cells];

        for (int i = 0; i < this.nb_cells; i++)
            for (int j = 0; j < this.nb_cells; j++)
                if (!this.isMined[i][j])
                {
                    int nb_mines_added = 0;

                    if (i > 0)
                    {
                        if (j > 0 && this.isMined[i - 1][j - 1])
                            nb_mines_added++;
                        if (j < this.nb_cells - 1 && this.isMined[i - 1][j + 1])
                            nb_mines_added++;
                        if (this.isMined[i - 1][j])
                            nb_mines_added++;
                    }

                    if (i < this.nb_cells - 1) {
                        if (j > 0 && this.isMined[i + 1][j - 1])
                            nb_mines_added++;
                        if (j < this.nb_cells - 1 && this.isMined[i + 1][j + 1])
                            nb_mines_added++;
                        if (this.isMined[i + 1][j])
                            nb_mines_added++;
                    }

                    if (j > 0 && this.isMined[i][j - 1])
                        nb_mines_added++;
                    if (j < this.nb_cells - 1 && this.isMined[i][j + 1])
                        nb_mines_added++;

                    ret[i][j] = nb_mines_added;
                }

        return ret;
    }

    private Paint get_col_number(int num)
    {
        Paint ret = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (num == 1)
            ret.setColor(0xFF0000FF);
        else if (num == 2)
            ret.setColor(0xFF00FF00);
        else if (num == 3)
            ret.setColor(0xFFFFFF00);
        else
            ret.setColor(0xFFFF0000);

        ret.setTextSize(70);
        return ret;
    }

    private void uncover_zero_cells(int pos_x, int pos_y)
    {
        if (pos_x >= 0 && pos_x < this.nb_cells && pos_y >= 0 && pos_y < this.nb_cells && this.isCovered[pos_x][pos_y])
        {
            this.isCovered[pos_x][pos_y] = false;

            if (this.numbers_in_cells[pos_x][pos_y] == 0)
            {
                this.uncover_zero_cells(pos_x - 1, pos_y - 1);
                this.uncover_zero_cells(pos_x - 1, pos_y);
                this.uncover_zero_cells(pos_x - 1, pos_y + 1);
                this.uncover_zero_cells(pos_x, pos_y - 1);
                this.uncover_zero_cells(pos_x, pos_y);
                this.uncover_zero_cells(pos_x, pos_y + 1);
                this.uncover_zero_cells(pos_x + 1, pos_y - 1);
                this.uncover_zero_cells(pos_x + 1, pos_y);
                this.uncover_zero_cells(pos_x + 1, pos_y + 1);
            }
        }
    }

    private int nb_mines;

    private Rect[][] grid;

    private Boolean[][] isCovered;
    private Boolean[][] isMined;
    private int[][] numbers_in_cells;

    private int size_x;
    private int size_y;
    private int nb_cells;
    private Paint col_covered;
    private Paint col_uncovered;
    private Paint col_mined;
    private Paint col_M;
}
