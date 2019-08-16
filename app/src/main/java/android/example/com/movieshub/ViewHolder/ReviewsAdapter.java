package android.example.com.movieshub.ViewHolder;

import android.example.com.movieshub.Model.Review;
import android.example.com.movieshub.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    List<Review> reviews;

    public ReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item,viewGroup,false);
        return new ReviewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder reviewsViewHolder, int i) {
        Review review = reviews.get(i);
        reviewsViewHolder.txtvAuthor.setText(review.getAuthor());
        reviewsViewHolder.txtvContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if(reviews == null)
            return 0;
        return reviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView txtvAuthor;
        TextView txtvContent;
        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvAuthor = itemView.findViewById(R.id.txtv_author_review_item);
            txtvContent = itemView.findViewById(R.id.txtv_content_review_item);
        }
    }
}
