// Views/ProductDetailsScreen.swift
import SwiftUI
import SDWebImageSwiftUI

struct ProductDetailsScreen: View {
    let product: Product

    var body: some View {
        VStack {
            WebImage(url: URL(string: product.thumbnail))
                .resizable()
                .placeholder(Image(systemName: "photo"))
                .indicator(.activity)
                .frame(height: 300)
                .clipShape(RoundedRectangle(cornerRadius: 8))

            Text(product.title)
                .font(.largeTitle)
                .padding()

            Text(product.description)
                .font(.body)
                .padding()

            Text(product.price.description)
                .font(.title)
                .padding()

            Spacer()
        }
        .padding()
    }
}

struct ProductDetailsScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProductDetailsScreen(product: Product(id: 1, title: "Sample Product", description: "This is a sample product description.", price: 99.99, thumbnail: "https://via.placeholder.com/150"))
    }
}
