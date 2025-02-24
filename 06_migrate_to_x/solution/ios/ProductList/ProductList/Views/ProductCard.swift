// Views/ProductCard.swift
import SwiftUI
import SDWebImageSwiftUI

struct ProductCard: View {
    let product: Product
    let onProductClick: (Int) -> Void

    var body: some View {
        HStack {
            WebImage(url: URL(string: product.thumbnail))
                .resizable()
                .placeholder {
                    Image(systemName: "photo")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 100, height: 100)
                        .foregroundColor(.gray)
                }
                .indicator(.activity)
                .transition(.fade(duration: 0.5)) // Fade Transition
                .scaledToFit()
                .frame(width: 100, height: 100)

            VStack(alignment: .leading) {
                Text(product.title)
                    .font(.headline)
                Text(product.description)
                    .font(.subheadline)
                    .foregroundColor(.gray)
                Text(product.price.description)
                    .font(.subheadline)
            }
            Spacer()
        }
        .padding()
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white))
        .shadow(radius: 2)
        .onTapGesture {
            onProductClick(product.id)
        }
    }
}
