//
//  FollowingView.swift
//  iosApp
//
//  Created by Vladislav on 21.09.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct UserView: View {
 
    var following:UserFollowing
    
    var body: some View {
            HStack(alignment: .center, spacing: nil, content: {
                Text(following.name)
            })
            .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity, alignment: .leading)
            .background(Color.black)
            .cornerRadius(10.0)
            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 0)
            .padding(.horizontal, 10)
            .padding(.vertical, 5)
        }
}

