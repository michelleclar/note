import type { Meta, StoryObj } from "@storybook/react";

import {  LoginPopup } from "./SignInModal";
const meta = {
    title: "UI/SignInModal",
    component: LoginPopup,
    parameters: {
        // More on how to position stories at: https://storybook.js.org/docs/configure/story-layout
        layout: "fullscreen",
    },
    tags: ["autodocs"],
} satisfies Meta<typeof LoginPopup>;

export default meta;
type Story = StoryObj<typeof meta>;

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const Primary: Story = {
    args: {
    },
};